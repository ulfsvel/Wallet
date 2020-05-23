package com.ulfsvel.wallet.btc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BitcoinConfig {

    @Value("${bitcoind.isTestnet:true}")
    private boolean isTestnet;

    @Bean
    public BitcoinSettings bitcoinSettings() {
        return new BitcoinSettings(isTestnet);
    }

}
