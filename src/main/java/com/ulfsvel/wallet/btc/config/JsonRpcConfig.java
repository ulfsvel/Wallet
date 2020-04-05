package com.ulfsvel.wallet.btc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulfsvel.wallet.btc.service.BitcoindJsonRpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JsonRpcConfig {

    private final ObjectMapper objectMapper;
    @Value("${bitcoind.endpoint:http://localhost:8332/}")
    private String endpoint;

    public JsonRpcConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public BitcoindJsonRpcService bitcoindJsonRpcService() {
        return new BitcoindJsonRpcService(endpoint, objectMapper);
    }
}
