package com.saadeh.TDD_Event_City.repositories;

import com.saadeh.TDD_Event_City.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City,Long> {
}
