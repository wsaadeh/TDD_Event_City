package com.saadeh.TDD_Event_City.controllers;

import com.saadeh.TDD_Event_City.dto.EventDTO;
import com.saadeh.TDD_Event_City.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    private EventService service;

    @GetMapping
    public ResponseEntity<Page<EventDTO>> findAll(Pageable pageable){
        Page<EventDTO> page = service.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<EventDTO> update(@PathVariable Long id, @Valid @RequestBody EventDTO dto){
        dto = service.update(id,dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @PostMapping
    public ResponseEntity<EventDTO> insert(@Valid @RequestBody EventDTO dto){
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}
