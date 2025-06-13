package com.ooproject.controller;

import com.ooproject.model.FavoriteStatus;
import com.ooproject.model.Kategori;
import com.ooproject.model.Menu;
import com.ooproject.model.TransaksiMenu;
import com.ooproject.repository.MenuRepository;
import com.ooproject.repository.TransaksiMenuRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/admin/menu_list")
public class AdminMenuController {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TransaksiMenuRepository transaksiMenuRepository;

    // Tampilkan halaman kelola menu
    @GetMapping
    public String showMenuPage(@RequestParam(required = false) String favorite,
                            @RequestParam(required = false) String kategori,
                            Model model) {
        List<Menu> menus;

        boolean hasFavorite = favorite != null && !favorite.isEmpty();
        boolean hasKategori = kategori != null && !kategori.isEmpty();

        if (!hasFavorite && !hasKategori) {
            menus = menuRepository.findAll();
        } else if (hasFavorite && !hasKategori) {
            menus = menuRepository.findByFavorite(FavoriteStatus.valueOf(favorite));
        } else if (!hasFavorite && hasKategori) {
            menus = menuRepository.findByKategori(Kategori.valueOf(kategori));
        } else {
            menus = menuRepository.findByFavoriteAndKategori(
                FavoriteStatus.valueOf(favorite),
                Kategori.valueOf(kategori)
            );
        }

        model.addAttribute("menus", menus);
        return "admin_menu"; // nama file HTML
    }

    // Handle form tambah menu (POST)
    @PostMapping("/add")
    public String addMenu(@RequestParam String nama,
                          @RequestParam double harga,
                          @RequestParam String deskripsi,
                          @RequestParam("gambar") MultipartFile gambarFile,
                          @RequestParam(required = false) String favorite,
                          RedirectAttributes redirectAttributes) throws IOException {

        Menu menu = new Menu();
        menu.setNama(nama);
        menu.setHarga(harga);
        menu.setDeskripsi(deskripsi);

        // Konversi manual ke enum dengan fallback jika invalid/null
        try {
            menu.setFavorite(FavoriteStatus.valueOf(favorite != null ? favorite : "NO"));
        } catch (IllegalArgumentException e) {
            menu.setFavorite(FavoriteStatus.NO);
        }

        if (!gambarFile.isEmpty()) {
            String originalFileName = gambarFile.getOriginalFilename();
            String fileName = StringUtils.cleanPath(originalFileName != null ? originalFileName : "default-coffe.jpg");

            Path uploadDir = Paths.get("images"); // folder uploads di project root, bisa diubah sesuai kebutuhan
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
        }
            try (InputStream inputStream = gambarFile.getInputStream()) {
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
            menu.setGambar(fileName);
    }

        menuRepository.save(menu);
        redirectAttributes.addFlashAttribute("success", "Menu berhasil ditambahkan!");
        return "redirect:/admin/menu_list";
    }

    @PostMapping("/edit")
    public String editMenu(@RequestParam Long id,
                        @RequestParam String nama,
                        @RequestParam double harga,
                        @RequestParam String deskripsi,
                        @RequestParam(required = false) String favorite,
                        @RequestParam String kategori,
                        RedirectAttributes redirectAttributes) {
        Menu menu = menuRepository.findById(id).orElse(null);
        if (menu != null) {
            menu.setNama(nama);
            menu.setHarga(harga);
            menu.setDeskripsi(deskripsi);

             try {
                menu.setFavorite(FavoriteStatus.valueOf(favorite != null ? favorite : "NO"));
            } catch (IllegalArgumentException e) {
                menu.setFavorite(FavoriteStatus.NO);
            }

            try {
                menu.setKategori(Kategori.valueOf(kategori));
            } catch (IllegalArgumentException e) {
                menu.setKategori(Kategori.COFFEE);
            }
            menuRepository.save(menu);
            redirectAttributes.addFlashAttribute("success", "Menu berhasil diperbarui!");
        }
        return "redirect:/admin/menu_list";
    }

    // Handle delete menu
    @Transactional
    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // if(!menuRepository.existsById(id)) {
            //     redirectAttributes.addFlashAttribute("error", "Menu tidak ditemukan.");
            //     return "redirect:/admin/menu_list";
            // }

            // Ambil data transaksi terkait
            List<TransaksiMenu> daftar = transaksiMenuRepository.findByMenuId(id);
            if(daftar != null && !daftar.isEmpty()) {
                transaksiMenuRepository.deleteAll(daftar);
            }
            menuRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Menu berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus menu. Mungkin sedang digunakan di data transaksi.");
            e.printStackTrace(); // log ke console
        }
        return "redirect:/admin/menu_list";
    }

    // Tambahkan nanti: edit & delete
}