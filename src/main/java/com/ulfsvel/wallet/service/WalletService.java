package com.ulfsvel.wallet.service;

import com.ulfsvel.wallet.repository.WalletRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import com.ulfsvel.shamir.SecretGroup;
import com.ulfsvel.shamir.SecretsFactory;
import com.ulfsvel.shamir.Share;
import com.ulfsvel.wallet.entity.Wallet;
import com.ulfsvel.wallet.service.SecureSecretStorage.SecureSecretStorage;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

@Component
public class WalletService {

    private final WalletRepository walletRepository;

    private final SecretsFactory secretsFactory;

    private final SecureSecretStorage secureSecretStorage;

    public WalletService(WalletRepository walletRepository, SecretsFactory secretsFactory, SecureSecretStorage secureSecretStorage) {
        this.walletRepository = walletRepository;
        this.secretsFactory = secretsFactory;
        this.secureSecretStorage = secureSecretStorage;
    }

    public Wallet createWallet(String password) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        Credentials credentials = Credentials.create(Keys.createEcKeyPair());
        ECKeyPair keyPair = credentials.getEcKeyPair();
        SecretGroup secretGroup = secretsFactory.createSecret(2, 3);
        String encryptionSalt = KeyGenerators.string().generateKey();
        BytesEncryptor privateKeyEncryptor = Encryptors.stronger(secretGroup.getActualSecret().toString(), encryptionSalt);
        BytesEncryptor encryptionKeyEncryptor = Encryptors.stronger(password, encryptionSalt);


        String encryptedPrivateKey = Base64.getEncoder().encodeToString(
                privateKeyEncryptor.encrypt(
                        keyPair.getPrivateKey().toString().getBytes()
                )
        );
        List<Share> sharesList = secretGroup.getShares();

        String encryptedEncryptionKey = Base64.getEncoder().encodeToString(
                encryptionKeyEncryptor.encrypt(
                        sharesList.get(2).toString().getBytes()
                )
        );

        Wallet wallet = new Wallet()
                .setEncryptedPrivateKey(encryptedPrivateKey)
                .setPublicKey(keyPair.getPublicKey().toString())
                .setPublicAddress(credentials.getAddress())
                .setSharedEncryptionKey(sharesList.get(1).toString())
                .setEncryptedEncryptionKey(encryptedEncryptionKey)
                .setEncryptionSalt(encryptionSalt);


        wallet = walletRepository.save(wallet);


        secureSecretStorage.setSecretForWallet(wallet, sharesList.get(0).toString());

        return wallet;
    }

    public BigInteger getPrivateKeyForWallet(@NotNull Wallet wallet, @NotNull String password) {
        List<Share> shares = new LinkedList<>();
        shares.add(
                new Share(
                        wallet.getSharedEncryptionKey()
                )
        );

        BytesEncryptor encryptionKeyEncryptor = Encryptors.stronger(password, wallet.getEncryptionSalt());
        shares.add(
                new Share(
                        new String(
                                encryptionKeyEncryptor.decrypt(
                                        Base64.getDecoder().decode(
                                                wallet.getEncryptedEncryptionKey().getBytes(StandardCharsets.UTF_8
                                                )
                                        )
                                )
                        )
                )
        );

        String encryptionKey = secretsFactory.rebuildSecret(shares).toString();
        BytesEncryptor privateKeyEncryptor = Encryptors.stronger(encryptionKey, wallet.getEncryptionSalt());

        return new BigInteger(
                new String(
                        privateKeyEncryptor.decrypt(
                                Base64.getDecoder().decode(
                                        wallet.getEncryptedPrivateKey().getBytes()
                                )
                        )
                )
        );
    }

    public BigInteger recoverPrivateKeyForWallet(@NotNull Wallet wallet) {
        List<Share> shares = new LinkedList<>();
        shares.add(new Share(wallet.getSharedEncryptionKey()));
        shares.add(new Share(secureSecretStorage.getSecretForWallet(wallet)));
        BytesEncryptor privateKeyEncryptor = Encryptors.stronger(
                secretsFactory.rebuildSecret(shares).toString(),
                wallet.getEncryptionSalt()
        );

        return new BigInteger(
                new String(
                        privateKeyEncryptor.decrypt(
                                Base64.getDecoder().decode(
                                        wallet.getEncryptedPrivateKey().getBytes()
                                )
                        )
                )
        );
    }
}
