package com.ooproject.controller;

import com.ooproject.dto.TransaksiDetailDTO;
import com.ooproject.model.User;
import com.ooproject.repository.TransaksiMenuRepository;
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
    private final TransaksiMenuRepository transaksiMenuRepository;

    // ✅ Constructor dengan dua parameter
    public AdminController(UserService userService, UserRepository userRepository, TransaksiMenuRepository transaksiMenuRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.transaksiMenuRepository = transaksiMenuRepository;
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
    public String lihatTransaksi(Model model) {
        List<TransaksiDetailDTO> transaksiList = transaksiMenuRepository.findAllTransaksiDetails();
        model.addAttribute("transaksiList", transaksiList);
        // Hitung total pendapatan dari semua transaksi
        double totalPendapatan = transaksiList.stream()
        .filter(t -> t.getTotal() != null)
            .mapToDouble(TransaksiDetailDTO::getTotal)
            .sum();
        model.addAttribute("totalPendapatan", totalPendapatan);
        return "admin_transaksi";
    }

    // @GetMapping("/transaksi")
    // public String lihatTransaksi(Model model) {
    //     List<TransaksiDetailDTO> transaksiList = transaksiMenuRepository.findAllTransaksiDetails();
    //     model.addAttribute("transaksiList", transaksiList);
    //     return "admin_transaksi"; // file html baru
    // }
}