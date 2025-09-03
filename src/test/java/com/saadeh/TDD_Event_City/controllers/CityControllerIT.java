package com.saadeh.TDD_Event_City.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saadeh.TDD_Event_City.dto.CityDTO;
import com.saadeh.TDD_Event_City.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CityControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private String clientUsername;
    private String clientPassword;
    private String adminUsername;
    private String adminPassword;
    private String clientToken;
    private String adminToken;
    private String invalidToken;

    @BeforeEach
    void setUp() throws Exception {
        clientUsername = "ana@gmail.com";
        clientPassword = "123456";
        adminUsername = "bob@gmail.com";
        adminPassword = "123456";
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
        invalidToken = adminToken + "xpto";//simulates a wrong token
    }

    @Test
    public void insertShouldReturn401WhenInvalidToken() throws Exception {
        CityDTO dto = new CityDTO(null, "Recife");
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result =
                mockMvc.perform(post("/cities")
                        .header("Authorization", "Bearer " + invalidToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void insertShouldReturn403WhenClientLogged() throws Exception{
        CityDTO dto = new CityDTO(null,"Recife");
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result =
                mockMvc.perform(post("/cities")
                        .header("Authorization", "Bearer " + clientToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isForbidden());
    }

    @Test
    public void insertShouldInsertResourceWhenAdminLoggedAndCorrectData() throws Exception{
        CityDTO dto = new CityDTO(null,"Recife");
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result =
                mockMvc.perform(post("/cities")
                        .header("Authorization","Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value("Recife"));
    }

    @Test
    public void insertShouldReturn422WhenAdminLoggedAndBlackName() throws Exception {
        CityDTO dto = new CityDTO(null, "   ");
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result =
                mockMvc.perform(post("/cities")
                        .header("Authorization", "Bearer" + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );
        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));
        result.andExpect(jsonPath("$.errors[0].message").value("Campo requerido"));
    }

    @Test
    public void findAllShouldReturnAllResourcesSortedByName() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/cities")
                        .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$[0].name").value("Belo Horizonte"));
        result.andExpect(jsonPath("$[1].name").value("Belém"));
        result.andExpect(jsonPath("$[2].name").value("Brasília"));
    }

    @Test
    public void insertShouldInsertResource() throws Exception {

        CityDTO dto = new CityDTO(null, "Recife");
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result =
                mockMvc.perform(post("/cities")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value("Recife"));
    }

    @Test
    public void deleteShouldReturnNoContentWhenIndependentId() throws Exception {

        Long independentId = 5L;

        ResultActions result =
                mockMvc.perform(delete("/cities/{id}", independentId));


        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenNonExistingId() throws Exception {

        Long nonExistingId = 50L;

        ResultActions result =
                mockMvc.perform(delete("/cities/{id}", nonExistingId));

        result.andExpect(status().isNotFound());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {

        Long dependentId = 1L;

        ResultActions result =
                mockMvc.perform(delete("/cities/{id}", dependentId));

        result.andExpect(status().isBadRequest());
    }
}
