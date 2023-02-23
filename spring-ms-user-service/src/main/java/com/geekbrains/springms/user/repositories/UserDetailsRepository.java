package com.geekbrains.springms.user.repositories;

import com.geekbrains.springms.user.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
