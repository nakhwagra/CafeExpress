package com.ooproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ooproject.model.TransaksiMenu;
import java.util.List;
@Repository
public interface TransaksiMenuRepository extends JpaRepository<TransaksiMenu, Long> {
    List<TransaksiMenu> findByMenuId(Long menuId);
}
