package com.ulfsvel.wallet.btc.entity;

import java.util.List;
import java.util.Map;

public class SignTransactionResult {

    private String hex;

    private Boolean complete;

    private List<Map<String, String>> errors;

    public String getHex() {
        return hex;
    }

    public SignTransactionResult setHex(String hex) {
        this.hex = hex;
        return this;
    }

    public Boolean getComplete() {
        return complete;
    }

    public SignTransactionResult setComplete(Boolean complete) {
        this.complete = complete;
        return this;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public SignTransactionResult setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
        return this;
    }
}
