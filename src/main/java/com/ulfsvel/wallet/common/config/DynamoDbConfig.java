package com.ulfsvel.wallet.common.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.ulfsvel.wallet.common.service.SecureSecretStorage.CloudSecureSecretStorage.CloudSecureSecretStorage;
import com.ulfsvel.wallet.common.service.SecureSecretStorage.SecureSecretStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfig {

    @Value("${dynamoDb.region:eu-west-1}")
    private String dynamoDbRegion;

    @Bean
    SecureSecretStorage cloudSecureSecretStorage() {
        return new CloudSecureSecretStorage(new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard().withRegion(dynamoDbRegion).build()));
    }

}
