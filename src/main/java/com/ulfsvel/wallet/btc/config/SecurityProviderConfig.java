package com.ulfsvel.wallet.btc.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

import java.security.Security;

@Configuration
public class SecurityProviderConfig {

    public SecurityProviderConfig() {
        Security.addProvider(new BouncyCastleProvider());
    }
}
