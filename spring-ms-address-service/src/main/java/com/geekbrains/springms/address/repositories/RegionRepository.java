package com.geekbrains.springms.address.repositories;

import com.geekbrains.springms.address.entities.City;
import com.geekbrains.springms.address.entities.Country;
import com.geekbrains.springms.address.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByNameAndCountry(String name, Country country);
    List<Region> findAllByCountry(Country country);
}
