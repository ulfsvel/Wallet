package com.ulfsvel.wallet.common.repository;

import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.enums.WalletType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {

    Optional<Wallet> findWalletByPublicAddressAndUserEmail(String publicAddress, String email);

    List<Wallet> findAllByWalletTypeAndUserEmail(WalletType walletType, String email);

}
