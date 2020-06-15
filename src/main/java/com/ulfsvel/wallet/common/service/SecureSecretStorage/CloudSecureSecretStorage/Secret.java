package com.ulfsvel.wallet.common.service.SecureSecretStorage.CloudSecureSecretStorage;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "wallet-backup-keys")
public class Secret {

    private Long walletId;
    private String secret;

    @DynamoDBHashKey(attributeName = "wallet-id")
    public Long getWalletId() {
        return this.walletId;
    }

    public Secret setWalletId(Long walletId) {
        this.walletId = walletId;
        return this;
    }

    @DynamoDBAttribute(attributeName = "secret")
    public String getSecret() {
        return secret;
    }

    public Secret setSecret(String secret) {
        this.secret = secret;
        return this;
    }

}
