package com.ooproject.controller;

import com.ooproject.model.Menu;
import com.ooproject.repository.MenuRepository;
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

@Controller
@RequestMapping("/admin/menu_list")
public class AdminMenuController {

    @Autowired
    private MenuRepository menuRepository;

    // Tampilkan halaman kelola menu
    @GetMapping
    public String showMenuPage(Model model) {
        List<Menu> menus = menuRepository.findAll();
        model.addAttribute("menus", menus);
        return "admin_menu"; // Sesuaikan dengan nama file HTML kamu
    }

    // Handle form tambah menu (POST)
    @PostMapping("/add")
    public String addMenu(@RequestParam String nama,
                          @RequestParam double harga,
                          @RequestParam String deskripsi,
                          @RequestParam("gambar") MultipartFile gambarFile) throws IOException {

        Menu menu = new Menu();
        menu.setNama(nama);
        menu.setHarga(harga);
        menu.setDeskripsi(deskripsi);

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
        return "redirect:/admin/menu_list";
    }

    // Tambahkan nanti: edit & delete
}