package com.ulfsvel.wallet.common.response;

import com.ulfsvel.wallet.common.entity.Wallet;

import java.util.Map;
import java.util.TreeMap;

public class WalletSecurityResponse {

    private Wallet wallet;

    private Map<String, Object> auxiliaryData;

    public WalletSecurityResponse(Wallet wallet) {
        this.wallet = wallet;
        this.auxiliaryData = new TreeMap<>();
    }

    public WalletSecurityResponse setData(String key, Object data) {
        this.auxiliaryData.put(key, data);

        return this;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Map<String, Object> getAuxiliaryData() {
        return auxiliaryData;
    }
}
