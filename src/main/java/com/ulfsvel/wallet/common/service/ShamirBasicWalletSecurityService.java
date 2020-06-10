package com.ulfsvel.wallet.common.service;

import com.ulfsvel.shamir.SecretGroup;
import com.ulfsvel.shamir.SecretsFactory;
import com.ulfsvel.shamir.Share;
import com.ulfsvel.wallet.common.entity.PasswordResetToken;
import com.ulfsvel.wallet.common.entity.UnencryptedWallet;
import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.entity.WalletCredentials;
import com.ulfsvel.wallet.common.entity.security.ShamirBasicSecurity;
import com.ulfsvel.wallet.common.repository.PasswordResetTokenRepository;
import com.ulfsvel.wallet.common.repository.WalletRepository;
import com.ulfsvel.wallet.common.repository.security.ShamirBasicSecurityRepository;
import com.ulfsvel.wallet.common.response.WalletSecurityResponse;
import com.ulfsvel.wallet.common.service.SecureSecretStorage.SecureSecretStorage;
import com.ulfsvel.wallet.common.types.WalletSecurityType;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class ShamirBasicWalletSecurityService implements WalletSecurityService {

    private final WalletRepository walletRepository;

    private final ShamirBasicSecurityRepository shamirBasicSecurityRepository;

    private final SecretsFactory secretsFactory;

    private final SecureSecretStorage secureSecretStorage;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public ShamirBasicWalletSecurityService(WalletRepository walletRepository, ShamirBasicSecurityRepository shamirBasicSecurityRepository, SecretsFactory secretsFactory, SecureSecretStorage secureSecretStorage, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.walletRepository = walletRepository;
        this.shamirBasicSecurityRepository = shamirBasicSecurityRepository;
        this.secretsFactory = secretsFactory;
        this.secureSecretStorage = secureSecretStorage;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public UnencryptedWallet decryptWallet(Wallet wallet, String password) {
        ShamirBasicSecurity shamirBasicSecurity = getShamirBasicSecurity(wallet);

        List<Share> shares = new LinkedList<>();
        shares.add(
                new Share(
                        shamirBasicSecurity.getSharedEncryptionKey()
                )
        );

        AesService encryptionKeyEncryptor = new AesService(password, shamirBasicSecurity.getEncryptionSalt());
        shares.add(
                new Share(
                        encryptionKeyEncryptor.decrypt(shamirBasicSecurity.getEncryptedEncryptionKey())
                )
        );

        String encryptionKey = secretsFactory.rebuildSecret(shares).toString();
        AesService privateKeyEncryptor = new AesService(encryptionKey, shamirBasicSecurity.getEncryptionSalt());


        String privateKey = privateKeyEncryptor.decrypt(wallet.getEncryptedPrivateKey());

        return new UnencryptedWallet(wallet, privateKey);
    }

    public UnencryptedWallet recoverWallet(Wallet wallet) {
        ShamirBasicSecurity shamirBasicSecurity = getShamirBasicSecurity(wallet);

        List<Share> shares = new LinkedList<>();
        shares.add(new Share(shamirBasicSecurity.getSharedEncryptionKey()));
        shares.add(new Share(secureSecretStorage.getSecretForWallet(wallet)));
        AesService privateKeyEncryptor = new AesService(
                secretsFactory.rebuildSecret(shares).toString(),
                shamirBasicSecurity.getEncryptionSalt()
        );


        String privateKey = privateKeyEncryptor.decrypt(wallet.getEncryptedPrivateKey());

        return new UnencryptedWallet(wallet, privateKey);
    }

    public Wallet encryptWallet(UnencryptedWallet unencryptedWallet, String password) {
        SecretGroup secretGroup = secretsFactory.createSecret(2, 3);
        String encryptionSalt = AesService.createSalt();
        AesService privateKeyEncryptor = new AesService(secretGroup.getActualSecret().toString(), encryptionSalt);
        AesService encryptionKeyEncryptor = new AesService(password, encryptionSalt);

        String encryptedPrivateKey = privateKeyEncryptor.encrypt(unencryptedWallet.getPrivateKey());
        List<Share> sharesList = secretGroup.getShares();

        String encryptedEncryptionKey = encryptionKeyEncryptor.encrypt(sharesList.get(2).toString());

        Wallet wallet = new Wallet(unencryptedWallet)
                .setEncryptedPrivateKey(encryptedPrivateKey)
                .setWalletSecurityType(WalletSecurityType.ShamirBasic);

        wallet = walletRepository.save(wallet);

        Optional<ShamirBasicSecurity> optionalShamirBasicSecurity = shamirBasicSecurityRepository.findShamirBasicSecurityByWallet(wallet);
        ShamirBasicSecurity shamirBasicSecurity = optionalShamirBasicSecurity.orElseGet(ShamirBasicSecurity::new);
        shamirBasicSecurity
                .setSharedEncryptionKey(sharesList.get(1).toString())
                .setEncryptedEncryptionKey(encryptedEncryptionKey)
                .setEncryptionSalt(encryptionSalt)
                .setWallet(wallet);

        shamirBasicSecurityRepository.save(shamirBasicSecurity);


        secureSecretStorage.setSecretForWallet(wallet, sharesList.get(0).toString());

        return wallet;
    }

    @Override
    public UnencryptedWallet decryptWallet(Wallet wallet, WalletCredentials walletCredentials) {
        return decryptWallet(wallet, getPasswordFromCredentials(walletCredentials));
    }

    @Override
    public UnencryptedWallet recoverWallet(Wallet wallet, WalletCredentials walletCredentials) {
        Optional<PasswordResetToken> optionalPasswordResetToken = passwordResetTokenRepository.findByToken(getTokenFromCredentials(walletCredentials));
        if (!optionalPasswordResetToken.isPresent()) {
            throw new ValidationException("Token not found.");
        }

        PasswordResetToken passwordResetToken = optionalPasswordResetToken.get();
        Calendar cal = Calendar.getInstance();
        if ((passwordResetToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            throw new ValidationException("Confirm token is expired.");
        }

        if (!passwordResetToken.getUser().getId().equals(wallet.getUser().getId())) {
            throw new ValidationException("Invalid token for user");
        }

        return recoverWallet(wallet);
    }

    @Override
    public WalletSecurityResponse encryptWallet(UnencryptedWallet unencryptedWallet, WalletCredentials walletCredentials) {
        return new WalletSecurityResponse(encryptWallet(unencryptedWallet, getPasswordFromCredentials(walletCredentials)));
    }

    @Override
    public boolean areEncryptCredentialsValid(WalletCredentials walletCredentials) {
        return areCredentialsValid(walletCredentials);
    }

    @Override
    public boolean areDecryptCredentialsValid(WalletCredentials walletCredentials) {
        return areCredentialsValid(walletCredentials);
    }

    @Override
    public boolean areRecoverCredentialsInvalid(WalletCredentials walletCredentials) {
        if (!walletCredentials.containsKey("confirmationCode")) {
            return true;
        }

        return !(walletCredentials.get("confirmationCode") instanceof String);
    }

    @Override
    public boolean isRecoveryNotAvailable() {
        return false;
    }

    private ShamirBasicSecurity getShamirBasicSecurity(Wallet wallet) {
        Optional<ShamirBasicSecurity> optionalShamirBasicSecurity = shamirBasicSecurityRepository.findShamirBasicSecurityByWallet(wallet);
        if (!optionalShamirBasicSecurity.isPresent()) {
            throw new RuntimeException("No decryption key for the current security type");
        }
        return optionalShamirBasicSecurity.get();
    }

    private boolean areCredentialsValid(WalletCredentials walletCredentials) {
        if (!walletCredentials.containsKey("password")) {
            return false;
        }
        return walletCredentials.get("password") instanceof String;
    }

    private String getPasswordFromCredentials(WalletCredentials walletCredentials) {
        return (String) walletCredentials.get("password");
    }

    private String getTokenFromCredentials(WalletCredentials walletCredentials) {
        return (String) walletCredentials.get("confirmationCode");
    }
}
