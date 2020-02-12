package com.ulfsvel.wallet.eth.request;

public class RecoverPrivateKeyRequest {

    private Long walletId;

    public Long getWalletId() {
        return walletId;
    }

    public RecoverPrivateKeyRequest setWalletId(Long walletId) {
        this.walletId = walletId;
        return this;
    }
}
