package com.ulfsvel.wallet.common.service.SecureSecretStorage.CloudSecureSecretStorage;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.service.SecureSecretStorage.SecureSecretStorage;
import org.jetbrains.annotations.NotNull;

public class CloudSecureSecretStorage implements SecureSecretStorage {

    private final DynamoDBMapper dynamoDBMapper;

    public CloudSecureSecretStorage(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public String getSecretForWallet(@NotNull Wallet wallet) {
        Secret secret = dynamoDBMapper.load(Secret.class, wallet.getId());
        if (secret != null) {
            return secret.getSecret();
        }

        throw new RuntimeException("No secret exists for wallet");
    }

    @Override
    public void setSecretForWallet(Wallet wallet, String secretString) {
        Secret secret = dynamoDBMapper.load(Secret.class, wallet.getId());
        if (secret == null) {
            secret = new Secret().setWalletId(wallet.getId());
        }
        secret.setSecret(secretString);

        dynamoDBMapper.save(secret);
    }
}
