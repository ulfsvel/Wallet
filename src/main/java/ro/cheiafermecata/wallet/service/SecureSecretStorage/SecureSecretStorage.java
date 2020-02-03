package ro.cheiafermecata.wallet.service.SecureSecretStorage;

import ro.cheiafermecata.wallet.entity.Wallet;

public interface SecureSecretStorage {

    String getSecretForWallet(Wallet wallet);

    void setSecretForWallet(Wallet wallet, String secret);
}
