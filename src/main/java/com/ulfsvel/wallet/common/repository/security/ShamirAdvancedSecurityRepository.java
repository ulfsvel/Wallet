package com.ulfsvel.wallet.common.repository.security;

import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.entity.security.ShamirAdvancedSecurity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ShamirAdvancedSecurityRepository extends CrudRepository<ShamirAdvancedSecurity, Long> {

    Optional<ShamirAdvancedSecurity> findAesShamirAdvancedByWallet(Wallet wallet);

}
