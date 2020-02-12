package com.ulfsvel.wallet.eth.request;

public class CreateWalletRequest {

    public String password;

    public String getPassword() {
        return password;
    }

    public CreateWalletRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
