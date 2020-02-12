package com.ulfsvel.wallet.common.service.SecureSecretStorage;

import com.ulfsvel.wallet.common.entiry.Wallet;

public interface SecureSecretStorage {

    String getSecretForWallet(Wallet wallet);

    void setSecretForWallet(Wallet wallet, String secret);
}
