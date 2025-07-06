package com.example.merchantonboarding.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OwnerDto {
    
    @JsonProperty("id")
    private String id;
    
    // Constructors
    public OwnerDto() {
    }
    
    public OwnerDto(String id) {
        this.id = id;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
}