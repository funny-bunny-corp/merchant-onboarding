package com.example.merchantonboarding.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class MerchantDocument {
    
    @NotNull
    @Column(name = "document_type", nullable = false)
    private String type;
    
    @NotNull
    @Column(name = "document_value", nullable = false)
    private String value;
    
    // Constructors
    public MerchantDocument() {
    }
    
    public MerchantDocument(String type, String value) {
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
    
    @Override
    public String toString() {
        return "MerchantDocument{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}