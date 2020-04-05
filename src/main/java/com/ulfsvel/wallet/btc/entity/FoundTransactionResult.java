package com.ulfsvel.wallet.btc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FoundTransactionResult {

    private String hex;

    private Double fee;

    private Integer changePosition;

    public String getHex() {
        return hex;
    }

    @JsonProperty("hex")
    public FoundTransactionResult setHex(String hex) {
        this.hex = hex;
        return this;
    }

    public Double getFee() {
        return fee;
    }

    @JsonProperty("fee")
    public FoundTransactionResult setFee(Double fee) {
        this.fee = fee;
        return this;
    }

    public Integer getChangePosition() {
        return changePosition;
    }

    @JsonProperty("changeposition")
    public FoundTransactionResult setChangePosition(Integer changePosition) {
        this.changePosition = changePosition;
        return this;
    }
}
