package com.ulfsvel.wallet.eth.repository;

import com.ulfsvel.wallet.eth.entity.EthTransaction;
import org.springframework.data.repository.CrudRepository;

public interface EthTransactionRepository extends CrudRepository<EthTransaction, Long> {
}
