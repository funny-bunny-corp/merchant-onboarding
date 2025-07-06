package com.example.merchantonboarding.controller.dto;

import com.example.merchantonboarding.domain.Address;
import com.example.merchantonboarding.domain.Merchant;
import com.example.merchantonboarding.domain.MerchantDocument;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class NewMerchantRequest {
    
    @NotNull
    @Valid
    private DocumentDto document;
    
    @NotNull
    private String name;
    
    private String email;
    
    @NotNull
    private LocalDateTime validUntil;
    
    @NotNull
    private LocalDateTime startRelationship;
    
    @NotNull
    @Valid
    private AddressDto address;
    
    // Constructors
    public NewMerchantRequest() {
    }
    
    public NewMerchantRequest(DocumentDto document, String name, String email, 
                              LocalDateTime validUntil, LocalDateTime startRelationship, AddressDto address) {
        this.document = document;
        this.name = name;
        this.email = email;
        this.validUntil = validUntil;
        this.startRelationship = startRelationship;
        this.address = address;
    }
    
    // Conversion method
    public Merchant toMerchant() {
        return new Merchant(
            this.name,
            this.email,
            this.validUntil,
            this.startRelationship,
            new MerchantDocument(this.document.getType(), this.document.getValue()),
            new Address(
                this.address.getStreet(),
                this.address.getNumber(),
                this.address.getComplement(),
                this.address.getNeighborhood(),
                this.address.getCity()
            )
        );
    }
    
    // Getters and Setters
    public DocumentDto getDocument() {
        return document;
    }
    
    public void setDocument(DocumentDto document) {
        this.document = document;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDateTime getValidUntil() {
        return validUntil;
    }
    
    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
    
    public LocalDateTime getStartRelationship() {
        return startRelationship;
    }
    
    public void setStartRelationship(LocalDateTime startRelationship) {
        this.startRelationship = startRelationship;
    }
    
    public AddressDto getAddress() {
        return address;
    }
    
    public void setAddress(AddressDto address) {
        this.address = address;
    }
}