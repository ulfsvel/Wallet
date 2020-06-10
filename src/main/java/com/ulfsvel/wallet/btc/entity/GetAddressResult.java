package com.ulfsvel.wallet.btc.entity;

import java.util.Map;

public class GetAddressResult {

    private Map<String, Object> result;

    public Map<String, Object> getResult() {
        return result;
    }

    public GetAddressResult setResult(Map<String, Object> result) {
        this.result = result;
        return this;
    }

}
