package com.ulfsvel.wallet.btc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulfsvel.wallet.btc.entity.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class BitcoindJsonRpcService {

    private static final String CREATE_RAW_TRANSACTION_METHOD = "createrawtransaction";

    private static final String FOUND_RAW_TRANSACTION_METHOD = "fundrawtransaction";

    private static final String SIGN_RAW_TRANSACTION_METHOD = "signrawtransactionwithkey";

    private static final String GET_BALANCE_METHOD = "listunspent";

    private static final String DECODE_TRANSACTION_METHOD = "decoderawtransaction";

    private final String endpoint;

    private final HttpHeaders headers;

    private final ObjectMapper objectMapper;

    public BitcoindJsonRpcService(String endpoint, String auth, ObjectMapper objectMapper) {
        this.endpoint = endpoint;
        this.objectMapper = objectMapper;
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON);

        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        this.headers.set("Authorization", authHeader);
    }

    private RestTemplate createClient() {
        return new RestTemplate();
    }

    private String createRequestData(String method, Object input) throws JsonProcessingException {
        Map<String, Object> requestData = new TreeMap<>();
        requestData.put("jsonrpc", "1.0");
        requestData.put("id", "ulfsvel");
        requestData.put("method", method);
        requestData.put("params", input);

        return objectMapper.writeValueAsString(requestData);
    }

    private <T> T callMethod(String method, Object input, Class<T> responseClass) throws JsonProcessingException {
        HttpEntity<String> request = new HttpEntity<>(
                createRequestData(method, input),
                headers
        );
        return createClient().postForObject(endpoint, request, responseClass);
    }

    public CreateTransactionResult createRawTransaction(CreateTransaction createRawTransaction) throws JsonProcessingException {
        return callMethod(CREATE_RAW_TRANSACTION_METHOD, createRawTransaction, CreateTransactionResult.class);
    }

    public FoundTransactionResult foundTransaction(FoundTransaction foundTransaction) throws JsonProcessingException {
        return callMethod(FOUND_RAW_TRANSACTION_METHOD, foundTransaction, FoundTransactionResult.class);
    }

    public SignTransactionResult signTransaction(SignTransaction signTransaction) throws JsonProcessingException {
        return callMethod(SIGN_RAW_TRANSACTION_METHOD, signTransaction, SignTransactionResult.class);
    }

    public SendTransactionResult sendTransaction(SendTransaction sendTransaction) throws JsonProcessingException {
        return callMethod(SIGN_RAW_TRANSACTION_METHOD, sendTransaction, SendTransactionResult.class);
    }

    public GetBalanceResult getBalance(GetBalance getBalance) throws JsonProcessingException {
        return callMethod(GET_BALANCE_METHOD, getBalance, GetBalanceResult.class);
    }

    public String decodeTransaction(DecodeTransaction decodeTransaction) throws JsonProcessingException {
        return callMethod(DECODE_TRANSACTION_METHOD, decodeTransaction, String.class);
    }

}
