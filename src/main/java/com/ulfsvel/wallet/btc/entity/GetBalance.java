package com.ulfsvel.wallet.btc.entity;

public class GetBalance {

    private String address;

    private Integer confirmations;

    public String getAddress() {
        return address;
    }

    public GetBalance setAddress(String address) {
        this.address = address;
        return this;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public GetBalance setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
        return this;
    }
}
