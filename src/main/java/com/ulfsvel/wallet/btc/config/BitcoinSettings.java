package com.ulfsvel.wallet.btc.config;

public class BitcoinSettings {

    private final boolean isTestnet;

    public BitcoinSettings(boolean isTestnet) {
        this.isTestnet = isTestnet;
    }

    public boolean isTestnet() {
        return isTestnet;
    }
}
