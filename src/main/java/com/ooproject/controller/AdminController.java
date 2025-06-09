package com.ooproject.controller;

import com.ooproject.model.User;
import com.ooproject.repository.UserRepository;
import com.ooproject.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;

    // ✅ Constructor dengan dua parameter
    public AdminController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        List<User> users = userRepository.findByRole("USER"); // Ambil semua user biasa
        model.addAttribute("users", users);
        return "admin_dashboard"; // Thymeleaf template
    }

    @GetMapping("/admin/menu")
    public String kelolaMenu() {
        return "admin_menu";
    }

    @GetMapping("/admin/transaksi")
    public String lihatTransaksi() {
        return "transaksi_list";
    }
}