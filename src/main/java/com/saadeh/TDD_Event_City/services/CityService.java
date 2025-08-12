package com.saadeh.TDD_Event_City.services;

import com.saadeh.TDD_Event_City.dto.CityDTO;
import com.saadeh.TDD_Event_City.entities.City;
import com.saadeh.TDD_Event_City.repositories.CityRepository;
import com.saadeh.TDD_Event_City.services.exceptions.DatabaseException;
import com.saadeh.TDD_Event_City.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    @Transactional
    public CityDTO insert(CityDTO dto){
        City entity = new City();
        entity.setName(dto.getName());
        repository.save(entity);
        return new CityDTO(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if (!repository.existsById(id)){
            throw new ResourceNotFoundException("Id not found " + id);
        }
        try {
            repository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation.");
        }

    }



}
