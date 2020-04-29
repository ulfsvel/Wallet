package com.ulfsvel.wallet.common.response;

public class WalletBalanceResponse {

    private String balance;

    public WalletBalanceResponse(String balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }

    public WalletBalanceResponse setBalance(String balance) {
        this.balance = balance;
        return this;
    }
}
