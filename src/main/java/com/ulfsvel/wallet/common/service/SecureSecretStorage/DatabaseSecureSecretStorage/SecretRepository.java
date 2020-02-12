package com.ulfsvel.wallet.common.service.SecureSecretStorage.DatabaseSecureSecretStorage;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SecretRepository extends CrudRepository<Secret, Long> {

    Optional<Secret> findSecretByWallet_Id(Long id);

}
