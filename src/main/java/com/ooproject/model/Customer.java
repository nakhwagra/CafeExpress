package com.ooproject.model;

import jakarta.persistence.Entity;

@Entity
public class Customer extends User { 
    private String email;
    private String name;

    public Customer() {}

    // Getters & Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}