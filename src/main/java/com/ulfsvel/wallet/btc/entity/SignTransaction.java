package com.ulfsvel.wallet.btc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

public class SignTransaction {

    private String hexString;

    private List<String> privateKeys;

    public SignTransaction() {
        this.privateKeys = new LinkedList<>();
    }

    @JsonProperty("hexstring")
    public String getHexString() {
        return hexString;
    }

    public SignTransaction setHexString(String hexString) {
        this.hexString = hexString;
        return this;
    }

    @JsonProperty("privkeys")
    public List<String> getPrivateKeys() {
        return privateKeys;
    }

    public SignTransaction setPrivateKeys(List<String> privateKeys) {
        this.privateKeys = privateKeys;
        return this;
    }

    public SignTransaction putPrivateKey(String privateKey) {
        this.privateKeys.add(privateKey);
        return this;
    }
}
