package com.trader.model;

import com.beemo.common.model.BaseUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Trader extends BaseUser {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String company;
    
    // Default constructor for JPA
    public Trader() {
        super();
        this.setRole("TRADER");
    }
    
    // Constructor with all fields
    public Trader(String username, String email, String password, String name, String phoneNumber, String company) {
        super(username, email, password, "TRADER");
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.company = company;
    }
    
    // Getters and setters for trader-specific fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
