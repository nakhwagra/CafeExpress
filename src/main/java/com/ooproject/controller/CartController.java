package com.ooproject.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.ui.Model;

import com.ooproject.model.CartItem;
import com.ooproject.model.Customer;
import com.ooproject.model.Menu;
import com.ooproject.model.Transaksi;
import com.ooproject.model.TransaksiMenu;
import com.ooproject.repository.MenuRepository;
import com.ooproject.repository.TransaksiMenuRepository;
import com.ooproject.repository.TransaksiRepository;


@Controller
@SessionAttributes("cart")

public class CartController {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private TransaksiMenuRepository transaksiMenuRepository;

    @ModelAttribute("cart")
    public List<CartItem> initCart() {
        return new ArrayList<>();
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long menuId, 
                            @RequestParam(defaultValue = "1") int jumlah,
                            @ModelAttribute("cart") List<CartItem> cart) {
        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu != null) {
            for (CartItem item : cart) {
                if (item.getMenu().getId().equals(menuId)) {
                    item.setJumlah(item.getJumlah() + jumlah);
                    return "redirect:/menu";
                }
            }
            cart.add(new CartItem(menu, jumlah));
        }
        return "redirect:/menu";
    }

    @GetMapping("/cart")
    public String viewCart(@ModelAttribute("cart") List<CartItem> cart, Model model) {
        double total = cart.stream().mapToDouble(CartItem::getTotalHarga).sum();
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long menuId, 
                                 @ModelAttribute("cart") List<CartItem> cart) {
        cart.removeIf(item -> item.getMenu().getId().equals(menuId));
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long menuId,
                            @RequestParam String action,
                            @ModelAttribute("cart") List<CartItem> cart) {
        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);
            if (item.getMenu().getId().equals(menuId)) {
                if ("increase".equals(action)) {
                    item.setJumlah(item.getJumlah() + 1);
                } else if ("decrease".equals(action)) {
                    item.setJumlah(item.getJumlah() - 1);
                    if (item.getJumlah() <= 0) {
                        cart.remove(i);
                        break;
                    }
                }
                break;
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(@ModelAttribute("cart") List<CartItem> cart, 
                           Principal principal) {
        // Simulasi customer login
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Dummy");

        // Buat transaksi
        Transaksi transaksi = new Transaksi();
        transaksi.setCustomer(customer);
        transaksi.setTanggal(LocalDateTime.now());
        transaksi.setTotal(cart.stream().mapToDouble(CartItem::getTotalHarga).sum());
        transaksi = transaksiRepository.save(transaksi);

        for (CartItem item : cart) {
            TransaksiMenu transaksiMenu = new TransaksiMenu();
            transaksiMenu.setMenu(item.getMenu());
            transaksiMenu.setJumlah(item.getJumlah());
            transaksiMenu.setTransaksi(transaksi);
            transaksiMenuRepository.save(transaksiMenu);
        }

        cart.clear(); // kosongkan keranjang
        return "redirect:/success";
    }   
}
