package com.ulfsvel.wallet.common.repository;

import com.ulfsvel.wallet.common.entity.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {

    Optional<Wallet> findWalletByPublicAddress(String publicAddress);

}
