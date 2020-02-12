package com.ulfsvel.wallet.common.service;

import com.ulfsvel.shamir.SecretGroup;
import com.ulfsvel.shamir.SecretsFactory;
import com.ulfsvel.shamir.Share;
import com.ulfsvel.wallet.common.entiry.Wallet;
import com.ulfsvel.wallet.common.entiry.security.ShamirBasicSecurity;
import com.ulfsvel.wallet.common.repository.WalletRepository;
import com.ulfsvel.wallet.common.repository.security.ShamirBasicSecurityRepository;
import com.ulfsvel.wallet.common.service.SecureSecretStorage.SecureSecretStorage;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class ShamirBasicSecurityWalletService {

    private final WalletRepository walletRepository;

    private final ShamirBasicSecurityRepository shamirBasicSecurityRepository;

    private final SecretsFactory secretsFactory;

    private final SecureSecretStorage secureSecretStorage;

    public ShamirBasicSecurityWalletService(WalletRepository walletRepository, ShamirBasicSecurityRepository shamirBasicSecurityRepository, SecretsFactory secretsFactory, SecureSecretStorage secureSecretStorage) {
        this.walletRepository = walletRepository;
        this.shamirBasicSecurityRepository = shamirBasicSecurityRepository;
        this.secretsFactory = secretsFactory;
        this.secureSecretStorage = secureSecretStorage;
    }

    public Wallet createWallet(String password) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        Credentials credentials = Credentials.create(Keys.createEcKeyPair());
        ECKeyPair keyPair = credentials.getEcKeyPair();
        SecretGroup secretGroup = secretsFactory.createSecret(2, 3);
        String encryptionSalt = AesService.createSalt();
        AesService privateKeyEncryptor = new AesService(secretGroup.getActualSecret().toString(), encryptionSalt);
        AesService encryptionKeyEncryptor = new AesService(password, encryptionSalt);


        String encryptedPrivateKey = privateKeyEncryptor.encrypt(keyPair.getPrivateKey().toString());
        List<Share> sharesList = secretGroup.getShares();

        String encryptedEncryptionKey = encryptionKeyEncryptor.encrypt(sharesList.get(2).toString());

        Wallet wallet = new Wallet()
                .setEncryptedPrivateKey(encryptedPrivateKey)
                .setPublicKey(keyPair.getPublicKey().toString())
                .setPublicAddress(credentials.getAddress())
                .setWalletSecurityType(Wallet.SHAMIR_BASIC_SECURITY);
        wallet = walletRepository.save(wallet);

        ShamirBasicSecurity shamirBasicSecurity = new ShamirBasicSecurity()
                .setSharedEncryptionKey(sharesList.get(1).toString())
                .setEncryptedEncryptionKey(encryptedEncryptionKey)
                .setEncryptionSalt(encryptionSalt)
                .setWallet(wallet);


        shamirBasicSecurityRepository.save(shamirBasicSecurity);


        secureSecretStorage.setSecretForWallet(wallet, sharesList.get(0).toString());

        return wallet;
    }

    public BigInteger getPrivateKeyForWallet(@NotNull Wallet wallet, @NotNull String password) {
        ShamirBasicSecurity shamirBasicSecurity = getShamirBasicSecurity(wallet);

        List<Share> shares = new LinkedList<>();
        shares.add(
                new Share(
                        shamirBasicSecurity.getSharedEncryptionKey()
                )
        );

        BytesEncryptor encryptionKeyEncryptor = Encryptors.stronger(password, shamirBasicSecurity.getEncryptionSalt());
        shares.add(
                new Share(
                        new String(
                                encryptionKeyEncryptor.decrypt(
                                        Base64.getDecoder().decode(
                                                shamirBasicSecurity.getEncryptedEncryptionKey().getBytes(StandardCharsets.UTF_8
                                                )
                                        )
                                )
                        )
                )
        );

        String encryptionKey = secretsFactory.rebuildSecret(shares).toString();
        BytesEncryptor privateKeyEncryptor = Encryptors.stronger(encryptionKey, shamirBasicSecurity.getEncryptionSalt());

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
        ShamirBasicSecurity shamirBasicSecurity = getShamirBasicSecurity(wallet);

        List<Share> shares = new LinkedList<>();
        shares.add(new Share(shamirBasicSecurity.getSharedEncryptionKey()));
        shares.add(new Share(secureSecretStorage.getSecretForWallet(wallet)));
        BytesEncryptor privateKeyEncryptor = Encryptors.stronger(
                secretsFactory.rebuildSecret(shares).toString(),
                shamirBasicSecurity.getEncryptionSalt()
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

    @NotNull
    private ShamirBasicSecurity getShamirBasicSecurity(@NotNull Wallet wallet) {
        Optional<ShamirBasicSecurity> optionalShamirBasicSecurity = shamirBasicSecurityRepository.findShamirBasicSecurityByWallet(wallet);
        if (!optionalShamirBasicSecurity.isPresent()) {
            throw new RuntimeException("No decryption key for the current security type");
        }
        return optionalShamirBasicSecurity.get();
    }
}
