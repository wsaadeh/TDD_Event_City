package com.saadeh.TDD_Event_City.repositories;

import com.saadeh.TDD_Event_City.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
