package com.ooproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ooproject.model.Transaksi;

public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
    
}
