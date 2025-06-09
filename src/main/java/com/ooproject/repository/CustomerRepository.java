package com.ooproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ooproject.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
    Customer findByUsername(String username);
    
}
