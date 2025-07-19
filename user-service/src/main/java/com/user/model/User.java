package com.user.model;

import com.beemo.common.model.BaseUser;

import jakarta.persistence.Entity;

@Entity
public class User extends BaseUser {
    public User() {
        super();
        this.setRole("USER");
    }
    
    public User(String username, String email, String password) {
        super(username, email, password, "USER");
    }
}