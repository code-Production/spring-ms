package com.geekbrains.springms.user.repositories;

import com.geekbrains.springms.user.entities.UserBilling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBillingRepository extends JpaRepository<UserBilling, Long> {

    Optional<UserBilling> findByCardNumber(String cardNumber);
}
