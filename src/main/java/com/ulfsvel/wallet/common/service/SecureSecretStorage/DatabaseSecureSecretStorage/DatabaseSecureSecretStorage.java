package com.ulfsvel.wallet.common.service.SecureSecretStorage.DatabaseSecureSecretStorage;

import com.ulfsvel.wallet.common.entiry.Wallet;
import com.ulfsvel.wallet.common.service.SecureSecretStorage.SecureSecretStorage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseSecureSecretStorage implements SecureSecretStorage {

    private final SecretRepository secretRepository;

    public DatabaseSecureSecretStorage(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    @Override
    public String getSecretForWallet(@NotNull Wallet wallet) {
        Optional<Secret> optionalSecret = secretRepository.findSecretByWallet_Id(wallet.getId());
        if (optionalSecret.isPresent()) {
            Secret secret = optionalSecret.get();
            return secret.getSecret();
        }

        throw new RuntimeException("No secret exists for wallet");
    }

    @Override
    public void setSecretForWallet(Wallet wallet, String secretString) {
        Secret secret = new Secret(wallet, secretString);
        secretRepository.save(secret);
    }
}
