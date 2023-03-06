package com.geekbrains.springms.address.repositories;

import com.geekbrains.springms.address.entities.City;
import com.geekbrains.springms.address.entities.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {
    Optional<Street> findByNameAndCity(String name, City city);
    List<Street> findAllByCity(City city);
}
