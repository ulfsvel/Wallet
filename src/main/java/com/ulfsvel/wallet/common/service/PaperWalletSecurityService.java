package com.ulfsvel.wallet.common.service;

import com.ulfsvel.wallet.common.entity.UnencryptedWallet;
import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.entity.WalletCredentials;
import com.ulfsvel.wallet.common.response.WalletSecurityResponse;
import org.springframework.stereotype.Component;

@Component
public class PaperWalletSecurityService implements WalletSecurityService {
    @Override
    public UnencryptedWallet decryptWallet(Wallet wallet, WalletCredentials walletCredentials) {
        return new UnencryptedWallet(wallet, getPrivateKeyFromCredentials(walletCredentials));
    }

    @Override
    public UnencryptedWallet recoverWallet(Wallet wallet, WalletCredentials walletCredentials) {
        return null;
    }

    @Override
    public WalletSecurityResponse encryptWallet(UnencryptedWallet unencryptedWallet, WalletCredentials walletCredentials) {
        return new WalletSecurityResponse(
                new Wallet(unencryptedWallet)
                        .setEncryptedPrivateKey("")
        ).setData("privateKey", unencryptedWallet.getPrivateKey());
    }

    @Override
    public boolean areEncryptCredentialsValid(WalletCredentials walletCredentials) {
        return false;
    }

    @Override
    public boolean areDecryptCredentialsValid(WalletCredentials walletCredentials) {
        if (!walletCredentials.containsKey("privateKey")) {
            return false;
        }
        return walletCredentials.get("privateKey") instanceof String;
    }

    private String getPrivateKeyFromCredentials(WalletCredentials walletCredentials) {
        return (String) walletCredentials.get("privateKey");
    }

    @Override
    public boolean areRecoverCredentialsInvalid(WalletCredentials walletCredentials) {
        return true;
    }

    @Override
    public boolean isRecoveryNotAvailable() {
        return true;
    }
}