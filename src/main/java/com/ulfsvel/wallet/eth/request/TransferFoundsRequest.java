package com.ulfsvel.wallet.eth.request;

public class TransferFoundsRequest {

    private Long walletId;

    private String password;

    private String to;

    private String amount;

    public Long getWalletId() {
        return walletId;
    }

    public TransferFoundsRequest setWalletId(Long walletId) {
        this.walletId = walletId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public TransferFoundsRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getTo() {
        return to;
    }

    public TransferFoundsRequest setTo(String to) {
        this.to = to;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public TransferFoundsRequest setAmount(String amount) {
        this.amount = amount;
        return this;
    }
}
