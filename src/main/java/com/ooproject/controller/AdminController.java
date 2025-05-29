package com.ooproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard() {
        return "admin_dashboard";
    }

    @GetMapping("/admin/menu")
    public String kelolaMenu() {
        return "admin_menu"; 
    }

    @GetMapping("/admin/transaksi")
    public String lihatTransaksi() {
        return "transaksi_list"; // Halaman lihat transaksi (buat nanti)
    }
}