package com.ooproject.controller;

import com.ooproject.model.Menu;
import com.ooproject.model.Customer;
import com.ooproject.model.Transaksi;
import com.ooproject.model.TransaksiMenu;
import com.ooproject.repository.MenuRepository;
import com.ooproject.repository.TransaksiRepository;
import com.ooproject.repository.TransaksiMenuRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private TransaksiMenuRepository transaksiMenuRepository;

    @GetMapping("/menu")
    public String Menu(Model model) {
        model.addAttribute("menus", menuRepository.findAll());
        return "menu";
    }

    @GetMapping("/menu/{id}")
    public String getMenuDetail(@PathVariable Long id, Model model) {
        Menu menu = menuRepository.findById(id).orElse(null);
        if (menu == null) {
            return "redirect:/menu";
        }

        model.addAttribute("menu", menu);
        return "menu_detail";
    }

    @PostMapping("/order")
    public String buatOrder(@RequestParam Long menuId,
                            @RequestParam int jumlah,
                            Principal principal) {
        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null || jumlah <= 0) {
            return "redirect:/menu";
        }

        // Simulasi customer, sesuaikan nanti dengan auth
        Customer customer = new Customer();
        customer.setId(1L); // HARDCODED — ganti dengan user login dari principal
        customer.setName("Dummy User");

        // Buat transaksi
        Transaksi transaksi = new Transaksi();
        transaksi.setTanggal(LocalDateTime.now());
        transaksi.setCustomer(customer);
        transaksi.setTotal(menu.getHarga() * jumlah);
        transaksi = transaksiRepository.save(transaksi);

        // Buat relasi transaksi_menu
        TransaksiMenu transaksiMenu = new TransaksiMenu();
        transaksiMenu.setMenu(menu);
        transaksiMenu.setJumlah(jumlah);
        transaksiMenu.setTransaksi(transaksi);
        transaksiMenuRepository.save(transaksiMenu);

        return "redirect:/success";
    }
}