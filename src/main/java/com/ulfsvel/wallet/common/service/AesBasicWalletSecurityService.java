package com.ulfsvel.wallet.common.service;

import com.ulfsvel.wallet.common.entity.UnencryptedWallet;
import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.entity.WalletCredentials;
import com.ulfsvel.wallet.common.entity.security.AesBasicSecurity;
import com.ulfsvel.wallet.common.enums.WalletSecurityType;
import com.ulfsvel.wallet.common.repository.WalletRepository;
import com.ulfsvel.wallet.common.repository.security.AesBasicSecurityRepository;
import com.ulfsvel.wallet.common.response.WalletSecurityResponse;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.Optional;

@Component
public class AesBasicWalletSecurityService implements WalletSecurityService {

    private final AesBasicSecurityRepository aesBasicSecurityRepository;
    private final WalletRepository walletRepository;

    public AesBasicWalletSecurityService(AesBasicSecurityRepository aesBasicSecurityRepository, WalletRepository walletRepository) {
        this.aesBasicSecurityRepository = aesBasicSecurityRepository;
        this.walletRepository = walletRepository;
    }

    public UnencryptedWallet decryptWallet(Wallet wallet, String password) {
        AesBasicSecurity aesBasicSecurity = getAesBasicSecurity(wallet);
        AesService privateKeyEncryptor = new AesService(password, aesBasicSecurity.getEncryptionSalt());
        String privateKey = privateKeyEncryptor.decrypt(wallet.getEncryptedPrivateKey());

        return new UnencryptedWallet(wallet, privateKey);
    }

    public Wallet encryptWallet(UnencryptedWallet unencryptedWallet, String password) {
        String encryptionSalt = AesService.createSalt();

        AesService privateKeyEncryptor = new AesService(password, encryptionSalt);

        Wallet wallet = new Wallet(unencryptedWallet)
                .setEncryptedPrivateKey(privateKeyEncryptor.encrypt(unencryptedWallet.getPrivateKey()))
                .setWalletSecurityType(WalletSecurityType.AesBasic);

        wallet = walletRepository.save(wallet);

        Optional<AesBasicSecurity> optionalAesBasicSecurity = aesBasicSecurityRepository.findAesBasicSecurityByWallet(wallet);
        AesBasicSecurity aesBasicSecurity = optionalAesBasicSecurity.orElseGet(AesBasicSecurity::new);
        aesBasicSecurity
                .setEncryptionSalt(encryptionSalt)
                .setWallet(wallet);

        aesBasicSecurityRepository.save(aesBasicSecurity);

        return wallet;
    }

    @Override
    public UnencryptedWallet decryptWallet(Wallet wallet, WalletCredentials walletCredentials) {
        return decryptWallet(wallet, getPasswordFromCredentials(walletCredentials));
    }

    @Override
    public UnencryptedWallet recoverWallet(Wallet wallet, WalletCredentials walletCredentials) {
        throw new ValidationException("Action not available for current security type");
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
        return true;
    }

    @Override
    public boolean isRecoveryNotAvailable() {
        return true;
    }

    private AesBasicSecurity getAesBasicSecurity(Wallet wallet) {
        Optional<AesBasicSecurity> optionalAesBasicSecurity = aesBasicSecurityRepository.findAesBasicSecurityByWallet(wallet);
        if (!optionalAesBasicSecurity.isPresent()) {
            throw new RuntimeException("No decryption key for the current security type");
        }
        return optionalAesBasicSecurity.get();
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
}
