package com.ulfsvel.wallet.common.service.SecureSecretStorage.DatabaseSecureSecretStorage;

import com.ulfsvel.wallet.common.entiry.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SecretRepository extends CrudRepository<Secret, Long> {

    Optional<Secret> findSecretByWallet(Wallet wallet);

}
