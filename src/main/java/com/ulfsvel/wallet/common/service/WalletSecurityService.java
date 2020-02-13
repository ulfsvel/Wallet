package com.ulfsvel.wallet.common.service;

import com.ulfsvel.wallet.common.entiry.UnencryptedWallet;
import com.ulfsvel.wallet.common.entiry.Wallet;
import com.ulfsvel.wallet.common.entiry.WalletCredentials;

public interface WalletSecurityService {

    UnencryptedWallet decryptWallet(Wallet wallet, WalletCredentials walletCredentials);

    UnencryptedWallet recoverWallet(Wallet wallet, WalletCredentials walletCredentials);

    Wallet encryptWallet(UnencryptedWallet unencryptedWallet, WalletCredentials walletCredentials);

    boolean areEncryptCredentialsValid(WalletCredentials walletCredentials);

    boolean areDecryptCredentialsValid(WalletCredentials walletCredentials);

    boolean areRecoverCredentialsValid(WalletCredentials walletCredentials);

}
