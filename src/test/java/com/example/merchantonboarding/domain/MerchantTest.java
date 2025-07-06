package com.example.merchantonboarding.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MerchantTest {

    private Merchant merchant;
    private LocalDateTime validUntil;
    private LocalDateTime startRelationship;
    private MerchantDocument document;
    private Address address;

    @BeforeEach
    void setUp() {
        validUntil = LocalDateTime.now().plusYears(1);
        startRelationship = LocalDateTime.now().plusDays(30);
        document = new MerchantDocument("CPF", "12345678901");
        address = new Address("Main Street", "123", "Apt 1", "Downtown", "New York");
        
        merchant = new Merchant("Test Merchant", "test@example.com", validUntil, startRelationship, document, address);
    }

    @Test
    void testMerchantCreation() {
        assertNotNull(merchant);
        assertEquals("Test Merchant", merchant.getName());
        assertEquals("test@example.com", merchant.getEmail());
        assertEquals(validUntil, merchant.getValidUntil());
        assertEquals(startRelationship, merchant.getStartRelationship());
        assertEquals(document, merchant.getDocument());
        assertEquals(address, merchant.getAddress());
        assertNotNull(merchant.getRegistrationDate());
    }

    @Test
    void testMerchantDefaultConstructor() {
        Merchant defaultMerchant = new Merchant();
        assertNotNull(defaultMerchant);
        assertNotNull(defaultMerchant.getRegistrationDate());
    }

    @Test
    void testMerchantSetters() {
        UUID newId = UUID.randomUUID();
        LocalDateTime newValidUntil = LocalDateTime.now().plusYears(2);
        LocalDateTime newStartRelationship = LocalDateTime.now().plusDays(60);
        MerchantDocument newDocument = new MerchantDocument("CNPJ", "12345678000123");
        Address newAddress = new Address("Second Street", "456", "Suite 2", "Uptown", "Boston");

        merchant.setId(newId);
        merchant.setName("Updated Merchant");
        merchant.setEmail("updated@example.com");
        merchant.setValidUntil(newValidUntil);
        merchant.setStartRelationship(newStartRelationship);
        merchant.setDocument(newDocument);
        merchant.setAddress(newAddress);

        assertEquals(newId, merchant.getId());
        assertEquals("Updated Merchant", merchant.getName());
        assertEquals("updated@example.com", merchant.getEmail());
        assertEquals(newValidUntil, merchant.getValidUntil());
        assertEquals(newStartRelationship, merchant.getStartRelationship());
        assertEquals(newDocument, merchant.getDocument());
        assertEquals(newAddress, merchant.getAddress());
    }

    @Test
    void testMerchantToString() {
        String merchantString = merchant.toString();
        assertNotNull(merchantString);
        assertTrue(merchantString.contains("Test Merchant"));
        assertTrue(merchantString.contains("test@example.com"));
    }
}