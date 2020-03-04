package com.ulfsvel.wallet.common.repository.security;

import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.entity.security.AesBasicSecurity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AesBasicSecurityRepository extends CrudRepository<AesBasicSecurity, Long> {

    Optional<AesBasicSecurity> findAesBasicSecurityByWallet(Wallet wallet);

}
