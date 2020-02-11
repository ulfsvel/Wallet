package com.ulfsvel.wallet.request;

public class WalletRecoverRequest {

    private Long walletId;

    public Long getWalletId() {
        return walletId;
    }

    public WalletRecoverRequest setWalletId(Long walletId) {
        this.walletId = walletId;
        return this;
    }
}
