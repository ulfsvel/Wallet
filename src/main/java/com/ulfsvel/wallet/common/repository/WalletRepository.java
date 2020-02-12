package com.ulfsvel.wallet.common.repository;

import com.ulfsvel.wallet.common.entiry.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
}
