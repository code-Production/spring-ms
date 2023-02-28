package com.geekbrains.springms.address.repositories;

import com.geekbrains.springms.address.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUsername(String username);
}
