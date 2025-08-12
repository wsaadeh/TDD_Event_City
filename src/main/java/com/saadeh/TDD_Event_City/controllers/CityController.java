package com.saadeh.TDD_Event_City.controllers;

import com.saadeh.TDD_Event_City.dto.CityDTO;
import com.saadeh.TDD_Event_City.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/cities")
public class CityController {

    @Autowired
    private CityService service;

    @GetMapping
    public List<CityDTO> findALL(){
        return service.findAll();
    }

}
