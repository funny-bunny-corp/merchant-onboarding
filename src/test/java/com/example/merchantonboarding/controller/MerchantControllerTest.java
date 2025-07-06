package com.example.merchantonboarding.controller;

import com.example.merchantonboarding.controller.dto.AddressDto;
import com.example.merchantonboarding.controller.dto.DocumentDto;
import com.example.merchantonboarding.controller.dto.NewMerchantRequest;
import com.example.merchantonboarding.domain.Address;
import com.example.merchantonboarding.domain.Merchant;
import com.example.merchantonboarding.domain.MerchantDocument;
import com.example.merchantonboarding.domain.service.MerchantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MerchantController.class)
class MerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchantService merchantService;

    @Autowired
    private ObjectMapper objectMapper;

    private NewMerchantRequest newMerchantRequest;
    private Merchant merchant;
    private UUID merchantId;

    @BeforeEach
    void setUp() {
        merchantId = UUID.randomUUID();
        
        DocumentDto documentDto = new DocumentDto("CPF", "12345678901");
        AddressDto addressDto = new AddressDto("Main Street", "123", "Apt 1", "Downtown", "New York");
        
        LocalDateTime validUntil = LocalDateTime.now().plusYears(1);
        LocalDateTime startRelationship = LocalDateTime.now().plusDays(30);
        
        newMerchantRequest = new NewMerchantRequest(
            documentDto, 
            "Test Merchant", 
            "test@example.com", 
            validUntil, 
            startRelationship, 
            addressDto
        );

        MerchantDocument document = new MerchantDocument("CPF", "12345678901");
        Address address = new Address("Main Street", "123", "Apt 1", "Downtown", "New York");
        
        merchant = new Merchant("Test Merchant", "test@example.com", validUntil, startRelationship, document, address);
        merchant.setId(merchantId);
    }

    @Test
    void testCreateMerchant() throws Exception {
        doNothing().when(merchantService).save(any(Merchant.class));

        mockMvc.perform(post("/api/merchants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMerchantRequest)))
                .andExpect(status().isCreated());

        verify(merchantService, times(1)).save(any(Merchant.class));
    }

    @Test
    void testCreateMerchantWithInvalidData() throws Exception {
        NewMerchantRequest invalidRequest = new NewMerchantRequest();
        // Missing required fields

        mockMvc.perform(post("/api/merchants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(merchantService, never()).save(any(Merchant.class));
    }

    @Test
    void testGetAllMerchants() throws Exception {
        List<Merchant> merchants = Arrays.asList(merchant);
        when(merchantService.findAllMerchants()).thenReturn(CompletableFuture.completedFuture(merchants));

        mockMvc.perform(get("/api/merchants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Merchant"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(merchantService, times(1)).findAllMerchants();
    }

    @Test
    void testGetMerchantById() throws Exception {
        when(merchantService.findMerchantById(merchantId)).thenReturn(CompletableFuture.completedFuture(merchant));

        mockMvc.perform(get("/api/merchants/{id}", merchantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Merchant"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(merchantService, times(1)).findMerchantById(merchantId);
    }

    @Test
    void testGetMerchantByIdNotFound() throws Exception {
        when(merchantService.findMerchantById(merchantId)).thenReturn(CompletableFuture.completedFuture(null));

        mockMvc.perform(get("/api/merchants/{id}", merchantId))
                .andExpect(status().isNotFound());

        verify(merchantService, times(1)).findMerchantById(merchantId);
    }

    @Test
    void testCreateMerchantWithServiceException() throws Exception {
        doThrow(new RuntimeException("Service error")).when(merchantService).save(any(Merchant.class));

        mockMvc.perform(post("/api/merchants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMerchantRequest)))
                .andExpect(status().isInternalServerError());

        verify(merchantService, times(1)).save(any(Merchant.class));
    }

    @Test
    void testGetAllMerchantsWithServiceException() throws Exception {
        CompletableFuture<List<Merchant>> failedFuture = CompletableFuture.failedFuture(new RuntimeException("Service error"));
        when(merchantService.findAllMerchants()).thenReturn(failedFuture);

        mockMvc.perform(get("/api/merchants"))
                .andExpect(status().isInternalServerError());

        verify(merchantService, times(1)).findAllMerchants();
    }
}