package com.saadeh.TDD_Event_City.repositories;

import com.saadeh.TDD_Event_City.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
