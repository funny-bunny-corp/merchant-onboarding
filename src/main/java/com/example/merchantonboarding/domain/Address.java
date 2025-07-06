package com.example.merchantonboarding.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Address {
    
    @NotNull
    @Column(name = "address_street", nullable = false)
    private String street;
    
    @NotNull
    @Column(name = "address_number", nullable = false)
    private String number;
    
    @Column(name = "address_complement")
    private String complement;
    
    @NotNull
    @Column(name = "address_neighborhood", nullable = false)
    private String neighborhood;
    
    @NotNull
    @Column(name = "address_city", nullable = false)
    private String city;
    
    // Constructors
    public Address() {
    }
    
    public Address(String street, String number, String complement, String neighborhood, String city) {
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
    
    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", complement='" + complement + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}