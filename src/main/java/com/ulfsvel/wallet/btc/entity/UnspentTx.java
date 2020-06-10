package com.ulfsvel.wallet.btc.entity;

public class UnspentTx {

    Integer vout;

    String txid;

    Double amount;

    Boolean safe;

    public Integer getVout() {
        return vout;
    }

    public UnspentTx setVout(Integer vout) {
        this.vout = vout;
        return this;
    }

    public String getTxid() {
        return txid;
    }

    public UnspentTx setTxid(String txid) {
        this.txid = txid;
        return this;
    }

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
