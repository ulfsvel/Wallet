package com.ulfsvel.wallet.common.response;


public class WalletTransactionResponse {

    private final String transactionIdentifier;

    public WalletTransactionResponse(String transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }

    public String getTransactionIdentifier() {
        return transactionIdentifier;
    }
}
