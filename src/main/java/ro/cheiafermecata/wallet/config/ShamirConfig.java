package ro.cheiafermecata.wallet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.cheiafermecata.shamir.BigNumbersGenerator;
import ro.cheiafermecata.shamir.SecretsFactory;

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
