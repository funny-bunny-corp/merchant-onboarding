package com.example.merchantonboarding.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateOwnerRequest {
    
    @JsonProperty("owner")
    private OwnerDto owner;
    
    // Constructors
    public CreateOwnerRequest() {
    }
    
    public CreateOwnerRequest(OwnerDto owner) {
        this.owner = owner;
    }
    
    // Getters and Setters
    public OwnerDto getOwner() {
        return owner;
    }
    
    public void setOwner(OwnerDto owner) {
        this.owner = owner;
    }
}