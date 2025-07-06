package com.example.merchantonboarding.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "merchants")
public class Merchant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull
    @Column(nullable = false)
    private String name;
    
    @Column
    private String email;
    
    @NotNull
    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;
    
    @NotNull
    @Column(name = "start_relationship", nullable = false)
    private LocalDateTime startRelationship;
    
    @NotNull
    @Embedded
    private MerchantDocument document;
    
    @NotNull
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;
    
    @NotNull
    @Embedded
    private Address address;
    
    // Constructors
    public Merchant() {
        this.registrationDate = LocalDateTime.now();
    }
    
    public Merchant(String name, String email, LocalDateTime validUntil, 
                    LocalDateTime startRelationship, MerchantDocument document, Address address) {
        this();
        this.name = name;
        this.email = email;
        this.validUntil = validUntil;
        this.startRelationship = startRelationship;
        this.document = document;
        this.address = address;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
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
    
    public MerchantDocument getDocument() {
        return document;
    }
    
    public void setDocument(MerchantDocument document) {
        this.document = document;
    }
    
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    @Override
    public String toString() {
        return "Merchant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", validUntil=" + validUntil +
                ", startRelationship=" + startRelationship +
                ", document=" + document +
                ", registrationDate=" + registrationDate +
                ", address=" + address +
                '}';
    }
}