package com.ulfsvel.wallet.common.service;

import com.ulfsvel.wallet.common.entity.UnencryptedWallet;
import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.entity.WalletCredentials;
import com.ulfsvel.wallet.common.response.WalletSecurityResponse;

public interface WalletSecurityService {

    UnencryptedWallet decryptWallet(Wallet wallet, WalletCredentials walletCredentials);

    UnencryptedWallet recoverWallet(Wallet wallet, WalletCredentials walletCredentials);

    WalletSecurityResponse encryptWallet(UnencryptedWallet unencryptedWallet, WalletCredentials walletCredentials);

    boolean areEncryptCredentialsValid(WalletCredentials walletCredentials);

    boolean areDecryptCredentialsValid(WalletCredentials walletCredentials);

    boolean areRecoverCredentialsInvalid(WalletCredentials walletCredentials);

    boolean isRecoveryNotAvailable();

}
