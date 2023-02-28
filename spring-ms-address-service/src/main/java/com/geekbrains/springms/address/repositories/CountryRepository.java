package com.geekbrains.springms.address.repositories;

import com.geekbrains.springms.address.entities.City;
import com.geekbrains.springms.address.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
}
