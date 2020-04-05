package com.ulfsvel.wallet.btc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendTransaction {

    private String hexString;

    @JsonProperty("hexstring")
    public String getHexString() {
        return hexString;
    }

    public SendTransaction setHexString(String hexString) {
        this.hexString = hexString;
        return this;
    }
}
