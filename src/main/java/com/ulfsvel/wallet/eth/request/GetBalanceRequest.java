package com.ulfsvel.wallet.eth.request;

public class GetBalanceRequest {

    private String publicAddress;

    public String getPublicAddress() {
        return publicAddress;
    }

    public GetBalanceRequest setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
        return this;
    }
}
