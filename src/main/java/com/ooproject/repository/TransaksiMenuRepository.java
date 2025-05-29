package com.ooproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ooproject.model.TransaksiMenu;

public interface TransaksiMenuRepository extends JpaRepository<TransaksiMenu, Long> {
    
}
