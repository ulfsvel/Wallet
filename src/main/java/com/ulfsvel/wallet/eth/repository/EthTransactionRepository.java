package com.ulfsvel.wallet.eth.repository;

import com.ulfsvel.wallet.eth.entity.EthTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EthTransactionRepository extends CrudRepository<EthTransaction, Long> {

    List<EthTransaction> findAllByWalletPublicAddressAndWalletUserEmail(String publicAddress, String email);

}
