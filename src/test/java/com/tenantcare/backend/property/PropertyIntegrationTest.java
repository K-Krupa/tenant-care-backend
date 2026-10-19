package com.tenantcare.backend.property;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class PropertyIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setup(){
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void shouldPerformFullPropertyCrudLifecycle() throws Exception {
        //REGISTER AND LOGIN
        String email = "crud_master@test.pl";
        String password = "SecretPassowrd123!";

        String registerJson = """
                {
                  "email": "%s",
                  "password": "%s",
                  "firstName": "Anna",
                  "lastName": "Nowak",
                  "role": "LANDLORD"
                }
                """.formatted(email, password);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isCreated());

        String loginJson = """
                {
                    "email": "%s",
                    "password" : "%s"
                }
                """.formatted(email, password);

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();
        String authHeader = "Bearer " + token;

        //CREATE PROPERTY (POST)
        String createPropertyJson = """
                {
                    "name": "Mieszkanie Startowe",
                    "address": "ul. Prosta 1, Katowice",
                    "rentAmount": 2000,
                    "area": 45.0
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/api/v1/properties")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPropertyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mieszkanie Startowe"))
                .andReturn();

        Long propertyId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        //GET PROPERTY (GET)
        mockMvc.perform(get("/api/v1/properties")
                .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(propertyId));

        //EDIT PROPERTY (PUT)

        String updatePropertyJson = """
                {
                "name": "Mieszkanie Premium",
                "address": "ul. Prosta 1, Katowice",
                "rentAmount": 3500,
                "area": 45.0
                }
                """;

        mockMvc.perform(put("/api/v1/properties/" + propertyId)
                .header("authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePropertyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mieszkanie Premium"))
                .andExpect(jsonPath("$.rentAmount").value(3500));

        //DELETE PROPERTY (DELETE)
        mockMvc.perform(delete("/api/v1/properties/" + propertyId)
                .header("Authorization", authHeader))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/properties")
                .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }
}
