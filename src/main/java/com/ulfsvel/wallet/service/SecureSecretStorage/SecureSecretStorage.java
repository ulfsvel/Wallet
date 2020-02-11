package com.ulfsvel.wallet.service.SecureSecretStorage;

import com.ulfsvel.wallet.entity.Wallet;

public interface SecureSecretStorage {

    String getSecretForWallet(Wallet wallet);

    void setSecretForWallet(Wallet wallet, String secret);
}
