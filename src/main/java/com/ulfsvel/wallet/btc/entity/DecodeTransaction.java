package com.ulfsvel.wallet.btc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecodeTransaction {

    private String hexString;

    @JsonProperty("hexstring")
    public String getHexString() {
        return hexString;
    }

    public DecodeTransaction setHexString(String hexString) {
        this.hexString = hexString;
        return this;
    }
}
