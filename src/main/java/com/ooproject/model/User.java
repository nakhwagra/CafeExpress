package com.ooproject.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class User { //super class
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role;

    // @OneToOne
    // @JoinColumn(name= "customer_id")
    // private Customer customer;

    // public Customer getCustomer() {
    //     return customer;
    // }
    // public void setCustomer(Customer customer) {
    //     this.customer = customer;
    // }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { 
        this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { 
        this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { 
        this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { 
        this.role = role; }
}