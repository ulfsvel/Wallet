package com.ulfsvel.wallet.common.config;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.ulfsvel.wallet.common.service.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailSenderConfig {

    @Value("${email.from:no-reply@wallet.ulfsvel.com}")
    private String emailFrom;

    @Value("${email.region:eu-west-1}")
    private String emailRegion;

    @Bean
    MessageService messageService() {
        return new MessageService(
                AmazonSimpleEmailServiceClientBuilder.standard().withRegion(emailRegion).build(),
                emailFrom
        );
    }
}