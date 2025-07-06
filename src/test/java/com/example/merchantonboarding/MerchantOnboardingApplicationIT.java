package com.example.merchantonboarding;

import com.example.merchantonboarding.controller.dto.AddressDto;
import com.example.merchantonboarding.controller.dto.DocumentDto;
import com.example.merchantonboarding.controller.dto.NewMerchantRequest;
import com.example.merchantonboarding.domain.Merchant;
import com.example.merchantonboarding.infrastructure.repository.JpaMerchantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class MerchantOnboardingApplicationIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JpaMerchantRepository jpaMerchantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        jpaMerchantRepository.deleteAll();
    }

    @Test
    void testFullMerchantWorkflow() throws Exception {
        // Create a new merchant request
        DocumentDto documentDto = new DocumentDto("CPF", "12345678901");
        AddressDto addressDto = new AddressDto("Main Street", "123", "Apt 1", "Downtown", "New York");
        LocalDateTime validUntil = LocalDateTime.now().plusYears(1);
        LocalDateTime startRelationship = LocalDateTime.now().plusDays(30);
        
        NewMerchantRequest request = new NewMerchantRequest(
            documentDto,
            "Integration Test Merchant",
            "integration@example.com",
            validUntil,
            startRelationship,
            addressDto
        );

        // Test creating a merchant
        mockMvc.perform(post("/api/merchants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Verify merchant was saved in database
        List<Merchant> merchants = jpaMerchantRepository.findAll();
        assertEquals(1, merchants.size());
        Merchant savedMerchant = merchants.get(0);
        assertEquals("Integration Test Merchant", savedMerchant.getName());
        assertEquals("integration@example.com", savedMerchant.getEmail());

        // Test getting all merchants
        mockMvc.perform(get("/api/merchants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Integration Test Merchant"))
                .andExpect(jsonPath("$[0].email").value("integration@example.com"));

        // Test getting merchant by ID
        mockMvc.perform(get("/api/merchants/{id}", savedMerchant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Test Merchant"))
                .andExpect(jsonPath("$.email").value("integration@example.com"));
    }

    @Test
    void testCreateMerchantWithValidation() throws Exception {
        // Test with invalid data (missing required fields)
        NewMerchantRequest invalidRequest = new NewMerchantRequest();
        
        mockMvc.perform(post("/api/merchants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        // Verify no merchant was saved
        List<Merchant> merchants = jpaMerchantRepository.findAll();
        assertEquals(0, merchants.size());
    }

    @Test
    void testGetNonExistentMerchant() throws Exception {
        // Test getting a non-existent merchant
        mockMvc.perform(get("/api/merchants/{id}", java.util.UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testMultipleMerchants() throws Exception {
        // Create multiple merchants
        for (int i = 1; i <= 3; i++) {
            DocumentDto documentDto = new DocumentDto("CPF", "1234567890" + i);
            AddressDto addressDto = new AddressDto("Street " + i, "123", "Apt " + i, "District " + i, "City " + i);
            LocalDateTime validUntil = LocalDateTime.now().plusYears(1);
            LocalDateTime startRelationship = LocalDateTime.now().plusDays(30);
            
            NewMerchantRequest request = new NewMerchantRequest(
                documentDto,
                "Merchant " + i,
                "merchant" + i + "@example.com",
                validUntil,
                startRelationship,
                addressDto
            );

            mockMvc.perform(post("/api/merchants")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // Verify all merchants were created
        mockMvc.perform(get("/api/merchants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        // Verify database contains all merchants
        List<Merchant> merchants = jpaMerchantRepository.findAll();
        assertEquals(3, merchants.size());
    }
}