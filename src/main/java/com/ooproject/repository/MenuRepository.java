package com.ooproject.repository;

import com.ooproject.model.FavoriteStatus;
import com.ooproject.model.Kategori;
import com.ooproject.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByFavorite(FavoriteStatus favorite);
    List<Menu> findByKategori(Kategori kategori);
    List<Menu> findByFavoriteAndKategori(FavoriteStatus favorite, Kategori kategori);
}