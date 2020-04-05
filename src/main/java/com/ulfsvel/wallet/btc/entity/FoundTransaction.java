package com.ulfsvel.wallet.btc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.TreeMap;

public class FoundTransaction {

    private String hexString;

    private Map<String, String> options;

    public FoundTransaction() {
        this.options = new TreeMap<>();
    }

    @JsonProperty("hexstring")
    public String getHexString() {
        return hexString;
    }

    public FoundTransaction setHexString(String hexString) {
        this.hexString = hexString;
        return this;
    }

    @JsonProperty("options")
    public Map<String, String> getOptions() {
        return options;
    }

    public FoundTransaction setChangeAddress(String value) {
        this.options.put("changeAddress", value);
        return this;
    }

    public FoundTransaction setFeeRate(String value) {
        this.options.put("feeRate", value);
        return this;
    }
}
