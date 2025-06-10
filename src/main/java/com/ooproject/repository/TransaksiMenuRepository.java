package com.ooproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ooproject.dto.TransaksiDetailDTO;
import com.ooproject.model.TransaksiMenu;
import java.util.List;
@Repository
public interface TransaksiMenuRepository extends JpaRepository<TransaksiMenu, Long> {
    List<TransaksiMenu> findByMenuId(Long menuId);

    @Query("""
        SELECT new com.ooproject.dto.TransaksiDetailDTO(
            t.id, t.tanggal, t.total, t.alamat, t.metodePembayaran,
            m.nama, tm.jumlah, u.username
        )
        FROM TransaksiMenu tm
        JOIN tm.transaksi t
        JOIN t.user u
        JOIN tm.menu m
    """)
    List<TransaksiDetailDTO> findAllTransaksiDetails();
}
