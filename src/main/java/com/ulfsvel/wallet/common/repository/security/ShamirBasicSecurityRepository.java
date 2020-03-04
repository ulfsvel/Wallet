package com.ulfsvel.wallet.common.repository.security;

import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.entity.security.ShamirBasicSecurity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ShamirBasicSecurityRepository extends CrudRepository<ShamirBasicSecurity, Long> {

    Optional<ShamirBasicSecurity> findShamirBasicSecurityByWallet(Wallet wallet);

}
