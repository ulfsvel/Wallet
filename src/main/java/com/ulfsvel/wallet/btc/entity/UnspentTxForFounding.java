package com.ulfsvel.wallet.btc.entity;

public class UnspentTxForFounding {

    Integer vout;

    String txid;

    public UnspentTxForFounding(UnspentTx unspentTx) {
        vout = unspentTx.getVout();
        txid = unspentTx.getTxid();
    }

    public Integer getVout() {
        return vout;
    }

    public UnspentTxForFounding setVout(Integer vout) {
        this.vout = vout;
        return this;
    }

    public String getTxid() {
        return txid;
    }

    public UnspentTxForFounding setTxid(String txid) {
        this.txid = txid;
        return this;
    }
}
