package com.ulfsvel.wallet.common.service;

import com.ulfsvel.shamir.SecretGroup;
import com.ulfsvel.shamir.SecretsFactory;
import com.ulfsvel.shamir.Share;
import com.ulfsvel.wallet.common.entity.UnencryptedWallet;
import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.entity.WalletCredentials;
import com.ulfsvel.wallet.common.entity.security.ShamirAdvancedSecurity;
import com.ulfsvel.wallet.common.repository.WalletRepository;
import com.ulfsvel.wallet.common.repository.security.ShamirAdvancedSecurityRepository;
import com.ulfsvel.wallet.common.response.WalletSecurityResponse;
import com.ulfsvel.wallet.common.types.WalletSecurityType;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class ShamirAdvancedWalletSecurityService implements WalletSecurityService {

    private final WalletRepository walletRepository;

    private final SecretsFactory secretsFactory;

    private final ShamirAdvancedSecurityRepository shamirAdvancedSecurityRepository;

    public ShamirAdvancedWalletSecurityService(WalletRepository walletRepository, SecretsFactory secretsFactory, ShamirAdvancedSecurityRepository shamirAdvancedSecurityRepository) {
        this.walletRepository = walletRepository;
        this.secretsFactory = secretsFactory;
        this.shamirAdvancedSecurityRepository = shamirAdvancedSecurityRepository;
    }

    @Override
    public UnencryptedWallet decryptWallet(Wallet wallet, WalletCredentials walletCredentials) {
        ShamirAdvancedSecurity shamirAdvancedSecurity = getShamirAdvancedSecurity(wallet);
        List<Share> shares = getShares(walletCredentials);

        if (shamirAdvancedSecurity.getSharesToRebuild() > shares.size()) {
            throw new RuntimeException("Not enough shares to rebuild");
        }

        AesService privateKeyEncryptor = new AesService(
                secretsFactory.rebuildSecret(shares).toString(),
                shamirAdvancedSecurity.getEncryptionSalt()
        );


        String privateKey = privateKeyEncryptor.decrypt(wallet.getEncryptedPrivateKey());

        return new UnencryptedWallet(wallet, privateKey);
    }

    @Override
    public UnencryptedWallet recoverWallet(Wallet wallet, WalletCredentials walletCredentials) {
        return null;
    }

    @Override
    public WalletSecurityResponse encryptWallet(UnencryptedWallet unencryptedWallet, WalletCredentials walletCredentials) {
        SecretGroup secretGroup = secretsFactory.createSecret(getSharesToRebuild(walletCredentials), getTotalShare(walletCredentials));
        String encryptionSalt = AesService.createSalt();

        AesService privateKeyEncryptor = new AesService(secretGroup.getActualSecret().toString(), encryptionSalt);

        Wallet wallet = new Wallet(unencryptedWallet)
                .setEncryptedPrivateKey(privateKeyEncryptor.encrypt(unencryptedWallet.getPrivateKey()))
                .setWalletSecurityType(WalletSecurityType.ShamirAdvanced);

        wallet = walletRepository.save(wallet);

        Optional<ShamirAdvancedSecurity> optionalShamirAdvancedSecurity = shamirAdvancedSecurityRepository.findAesShamirAdvancedByWallet(wallet);
        ShamirAdvancedSecurity shamirAdvancedSecurity = optionalShamirAdvancedSecurity.orElseGet(ShamirAdvancedSecurity::new);
        shamirAdvancedSecurity
                .setSharesToRebuild(secretGroup.getSharesToRebuild())
                .setTotalShares(secretGroup.getTotalShares())
                .setEncryptionSalt(encryptionSalt)
                .setWallet(wallet);

        shamirAdvancedSecurityRepository.save(shamirAdvancedSecurity);

        List<String> shares = new LinkedList<>();
        for (Share share : secretGroup.getShares()) {
            shares.add(share.toString());
        }

        return new WalletSecurityResponse(wallet)
                .setData("shares", shares)
                .setData("sharesToRebuild", secretGroup.getSharesToRebuild())
                .setData("totalShares", secretGroup.getTotalShares());
    }

    @Override
    public boolean areEncryptCredentialsValid(WalletCredentials walletCredentials) {
        if (!walletCredentials.containsKey("sharesToRebuild")) {
            return false;
        }
        if (!walletCredentials.containsKey("totalShares")) {
            return false;
        }

        return walletCredentials.get("sharesToRebuild") instanceof Integer && walletCredentials.get("totalShares") instanceof Integer;
    }

    private Integer getSharesToRebuild(WalletCredentials walletCredentials) {
        return (Integer) walletCredentials.get("sharesToRebuild");
    }

    private Integer getTotalShare(WalletCredentials walletCredentials) {
        return (Integer) walletCredentials.get("totalShares");
    }

    @Override
    public boolean areDecryptCredentialsValid(WalletCredentials walletCredentials) {
        if (!walletCredentials.containsKey("shares")) {
            return false;
        }

        if (!(walletCredentials.get("shares") instanceof List<?>)) {
            return false;
        }
        List<?> list = (List<?>) walletCredentials.get("shares");
        for (Object element : list) {
            if (!(element instanceof Share)) {
                return false;
            }
        }
        return true;
    }

    private List<Share> getShares(WalletCredentials walletCredentials) {
        //noinspection unchecked
        return (List<Share>) walletCredentials.get("shares");
    }

    @Override
    public boolean areRecoverCredentialsInvalid(WalletCredentials walletCredentials) {
        return true;
    }

    @Override
    public boolean isRecoveryNotAvailable() {
        return true;
    }

    private ShamirAdvancedSecurity getShamirAdvancedSecurity(Wallet wallet) {
        Optional<ShamirAdvancedSecurity> optionalShamirAdvancedSecurity = shamirAdvancedSecurityRepository.findAesShamirAdvancedByWallet(wallet);
        if (!optionalShamirAdvancedSecurity.isPresent()) {
            throw new RuntimeException("No decryption key for the current security type");
        }
        return optionalShamirAdvancedSecurity.get();
    }
}
