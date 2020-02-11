package com.ulfsvel.wallet.config;

import com.ulfsvel.shamir.BigNumbersGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ulfsvel.shamir.SecretsFactory;

import java.security.SecureRandom;

@Configuration
public class ShamirConfig {

    @Value("${shamir.numberOfBitsForSecret:127}")
    private Integer numberOfBitsForSecret;

    @Bean
    public SecretsFactory secretsFactory() {
        return new SecretsFactory(
                new BigNumbersGenerator(
                        new SecureRandom()
                ),
                numberOfBitsForSecret
        );
    }

}
