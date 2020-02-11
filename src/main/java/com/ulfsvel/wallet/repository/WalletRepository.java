package com.ulfsvel.wallet.repository;

import org.springframework.data.repository.CrudRepository;
import com.ulfsvel.wallet.entity.Wallet;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
}
