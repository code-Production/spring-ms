package com.geekbrains.springms.user.repositories;

import com.geekbrains.springms.user.entities.UserBilling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBillingRepository extends JpaRepository<UserBilling, Long> {

    Optional<UserBilling> findByCardNumber(String cardNumber);
}
