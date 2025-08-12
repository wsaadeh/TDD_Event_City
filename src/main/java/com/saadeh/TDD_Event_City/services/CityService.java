package com.saadeh.TDD_Event_City.services;

import com.saadeh.TDD_Event_City.dto.CityDTO;
import com.saadeh.TDD_Event_City.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository repository;

    @Transactional(readOnly = true)
    public List<CityDTO> findAll(){
        return repository
                .findAll(Sort.by("name"))
                .stream()
                .map(x->new CityDTO(x))
                .toList();
    }



}
