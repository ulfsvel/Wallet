package com.ulfsvel.wallet.btc.entity;

public class HexRawTransaction {

    private String hexValue;

    public HexRawTransaction(String hexValue) {
        this.hexValue = hexValue;
    }

    public String getHexValue() {
        return hexValue;
    }

    public HexRawTransaction setHexValue(String hexValue) {
        this.hexValue = hexValue;
        return this;
    }
}
