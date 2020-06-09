package com.ulfsvel.wallet.btc.entity;

import java.util.List;

public class GetBalanceResult {

    private List<UnspentTx> result;

    public List<UnspentTx> getResult() {
        return result;
    }

    public GetBalanceResult setResult(List<UnspentTx> result) {
        this.result = result;
        return this;
    }
}
