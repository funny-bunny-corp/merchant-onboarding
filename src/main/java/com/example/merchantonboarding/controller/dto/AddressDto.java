package com.example.merchantonboarding.controller.dto;

import jakarta.validation.constraints.NotNull;

public class AddressDto {
    
    @NotNull
    private String street;
    
    @NotNull
    private String number;
    
    private String complement;
    
    @NotNull
    private String neighborhood;
    
    @NotNull
    private String city;
    
    // Constructors
    public AddressDto() {
    }
    
    public AddressDto(String street, String number, String complement, String neighborhood, String city) {
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
    }
    
    // Getters and Setters
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getNumber() {
        return number;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    
    public String getComplement() {
        return complement;
    }
    
    public void setComplement(String complement) {
        this.complement = complement;
    }
    
    public String getNeighborhood() {
        return neighborhood;
    }
    
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
}