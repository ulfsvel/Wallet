package com.ulfsvel.wallet.common.service.SecureSecretStorage;

import com.ulfsvel.wallet.common.entity.Wallet;

public interface SecureSecretStorage {

    String getSecretForWallet(Wallet wallet);

    void setSecretForWallet(Wallet wallet, String secret);
}
