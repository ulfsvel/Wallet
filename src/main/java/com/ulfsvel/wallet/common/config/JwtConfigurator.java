package com.ulfsvel.wallet.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfigurator {

    @Value("${jwt.authUrl:/api/authenticate}")
    private String authUrl;

    @Value("${jwt.issuer:wallet-api}")
    private String tokenIssuer;

    @Value("${jwt.audience:wallet-app}")
    private String tokenAudience;

    @Value("${jwt.secret:n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf}")
    private String secret;

    @Value("${jwt.expirationTime:864000000}")
    private Long expirationTime;

    @Value("${jwt.claimsName:roles}")
    private String claimsName;

    @Value("${jwt.typeName:type}")
    private String typeName;

    @Bean
    public JwtConfiguration jwtConfiguration() {
        return new JwtConfiguration(authUrl, tokenIssuer, tokenAudience, secret, expirationTime, claimsName, typeName);
    }

}
