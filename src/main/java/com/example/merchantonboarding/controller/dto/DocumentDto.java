package com.example.merchantonboarding.controller.dto;

import jakarta.validation.constraints.NotNull;

public class DocumentDto {
    
    @NotNull
    private String type;
    
    @NotNull
    private String value;
    
    // Constructors
    public DocumentDto() {
    }
    
    public DocumentDto(String type, String value) {
        this.type = type;
        this.value = value;
    }
    
    // Getters and Setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}