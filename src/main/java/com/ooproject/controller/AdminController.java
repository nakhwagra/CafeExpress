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

    public AdminController(UserService userService, UserRepository userRepository, TransaksiMenuRepository transaksiMenuRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.transaksiMenuRepository = transaksiMenuRepository;
    }

    /**
     * Menampilkan halaman dashboard admin dengan ringkasan data.
     * @param model Model untuk meneruskan data ringkasan ke template.
     * @return Nama template HTML untuk dashboard admin.
     */
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        // Menghitung jumlah total user terdaftar (dengan role "USER")
        // Karena userRepository Anda memiliki findByRole, kita bisa gunakan jumlah dari itu.
        // Atau jika ingin semua user tanpa filter role, gunakan userRepository.count();
        long totalUsers = userRepository.count(); // ✅ Menggunakan count() dari JpaRepository
        // long totalUsers = userRepository.findByRole("USER").size(); // Atau bisa pakai ini jika hanya ingin user biasa

        model.addAttribute("totalUsers", totalUsers); // ✅ Meneruskan jumlah user ke template
        
        // Hapus baris ini karena daftar user tidak lagi ditampilkan langsung di dashboard:
        List<User> users = userRepository.findByRole("USER");
        model.addAttribute("users", users);

        return "admin_dashboard";
    }

    /**
     * Menampilkan halaman kelola menu.
     * @return String nama template HTML untuk halaman kelola menu.
     */
    @GetMapping("/admin/menu") // Sesuaikan jika endpoint menu Anda berbeda
    public String kelolaMenu() {
        return "admin_menu"; // Atau redirect ke halaman daftar menu admin yang lebih spesifik
    }

    /**
     * Menampilkan halaman daftar transaksi.
     * @param model Model untuk meneruskan daftar transaksi dan total pendapatan.
     * @return Nama template HTML untuk daftar transaksi.
     */
    @GetMapping("/admin/transaksi")
    public String lihatTransaksi(Model model) {
        List<TransaksiDetailDTO> transaksiList = transaksiMenuRepository.findAllTransaksiDetails();
        model.addAttribute("transaksiList", transaksiList);

        double totalPendapatan = transaksiList.stream()
            .filter(t -> t.getTotal() != null)
            .mapToDouble(TransaksiDetailDTO::getTotal)
            .sum();
        model.addAttribute("totalPendapatan", totalPendapatan);
        return "admin_transaksi";
    }

    /**
     * ✅ Endpoint baru: Menampilkan halaman daftar semua user.
     * @param model Model untuk meneruskan daftar user lengkap ke template.
     * @return Nama template HTML untuk halaman daftar user.
     */
    @GetMapping("/admin/users")
    public String showAllUsers(Model model) {
        // Ambil semua user dari database
        List<User> users = userRepository.findAll(); // Atau jika ingin user biasa: userRepository.findByRole("USER");
        model.addAttribute("users", users); // Meneruskan daftar user lengkap
        return "admin_users"; // Nama template HTML baru Anda (yang berisi tabel user)
    }
}