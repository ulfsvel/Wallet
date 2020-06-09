package com.ulfsvel.wallet.btc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.TreeMap;

public class FoundTransaction {

    private String hexString;

    private final Map<String, Object> options;

    public FoundTransaction() {
        this.options = new TreeMap<>();
        options.put("includeWatching", Boolean.TRUE);
        options.put("estimate_mode", "ECONOMICAL");
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
    public Map<String, Object> getOptions() {
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
