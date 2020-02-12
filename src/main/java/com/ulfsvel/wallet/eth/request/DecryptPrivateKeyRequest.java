package com.ulfsvel.wallet.eth.request;

public class DecryptPrivateKeyRequest {

    private Long walletId;

    private String password;

    public Long getWalletId() {
        return walletId;
    }

    public DecryptPrivateKeyRequest setWalletId(Long walletId) {
        this.walletId = walletId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DecryptPrivateKeyRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
