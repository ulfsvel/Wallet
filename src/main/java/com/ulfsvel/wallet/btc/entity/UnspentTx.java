package com.ulfsvel.wallet.btc.entity;

public class UnspentTx {

    Double amount;

    Boolean safe;

    public Double getAmount() {
        return amount;
    }

    public UnspentTx setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Boolean getSafe() {
        return safe;
    }

    public UnspentTx setSafe(Boolean safe) {
        this.safe = safe;
        return this;
    }
}
