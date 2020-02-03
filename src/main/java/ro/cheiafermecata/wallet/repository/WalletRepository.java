package ro.cheiafermecata.wallet.repository;

import org.springframework.data.repository.CrudRepository;
import ro.cheiafermecata.wallet.entity.Wallet;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
}
