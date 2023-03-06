package com.geekbrains.springms.address.repositories;

import com.geekbrains.springms.address.entities.City;
import com.geekbrains.springms.address.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByNameAndRegion(String name, Region region);
    List<City> findAllByRegion(Region region);
}
