package com.saadeh.TDD_Event_City.services;

import com.saadeh.TDD_Event_City.dto.EventDTO;
import com.saadeh.TDD_Event_City.entities.Event;
import com.saadeh.TDD_Event_City.repositories.CityRepository;
import com.saadeh.TDD_Event_City.repositories.EventRepository;
import com.saadeh.TDD_Event_City.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Autowired
    private CityRepository cityRepository;

    @Transactional
    public EventDTO update(Long id, EventDTO dto){
        try {
            Event entity = repository.getReferenceById(id);
            copyDtoToEntity(entity, dto);
            repository.save(entity);
            return new EventDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    private void copyDtoToEntity(Event entity, EventDTO dto) {
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        entity.setUrl(dto.getUrl());
        entity.setCity(cityRepository.getReferenceById(dto.getCityId()));
    }

    @Transactional
    public EventDTO insert(EventDTO dto) {
        Event entity = new Event();
        copyDtoToEntity(entity,dto);
        repository.save(entity);
        return new EventDTO(entity);
    }
}
