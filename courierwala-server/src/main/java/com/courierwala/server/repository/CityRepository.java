package com.courierwala.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courierwala.server.entities.City;
import com.courierwala.server.entities.User;

public interface CityRepository extends JpaRepository<City, Long>{

	Optional<City> findByCityNameIgnoreCase(String cityName);

}
