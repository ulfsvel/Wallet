package com.ulfsvel.wallet.common.repository;

import com.ulfsvel.wallet.common.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
