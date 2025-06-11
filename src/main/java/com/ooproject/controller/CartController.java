package com.ooproject.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.ui.Model;

import org.springframework.transaction.annotation.Transactional;

import com.ooproject.model.CartItem;
import com.ooproject.model.Menu;
import com.ooproject.model.Transaksi;
import com.ooproject.model.TransaksiMenu;
import com.ooproject.model.User;
import com.ooproject.repository.MenuRepository;
import com.ooproject.repository.TransaksiMenuRepository;
import com.ooproject.repository.TransaksiRepository;
import com.ooproject.repository.UserRepository;

@Controller
@SessionAttributes("cart")
public class CartController {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private TransaksiMenuRepository transaksiMenuRepository;

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute("cart")
    public List<CartItem> initCart() {
        return new ArrayList<>();
    }

    /**
     * Menambah item menu ke keranjang belanja.
     * Item akan ditambahkan ke keranjang yang sudah ada.
     * Redirects ke halaman menu.
     * @param menuId ID menu yang akan ditambahkan.
     * @param jumlah Jumlah menu yang akan ditambahkan (default 1).
     * @param cart Objek keranjang dari sesi.
     * @return String redirect URL ke halaman menu.
     */
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long menuId,
                            @RequestParam(defaultValue = "1") int jumlah,
                            @ModelAttribute("cart") List<CartItem> cart) {
        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null) {
            System.out.println("Menu dengan ID " + menuId + " tidak ditemukan.");
            return "redirect:/menu";
        }

        boolean found = false;
        for (CartItem item : cart) {
            if (item.getMenu().getId().equals(menuId)) {
                item.setJumlah(item.getJumlah() + jumlah);
                found = true;
                break;
            }
        }
        if (!found) {
            cart.add(new CartItem(menu, jumlah));
        }

        System.out.println("Item '" + menu.getNama() + "' ditambahkan ke keranjang. Jumlah: " + jumlah);
        return "redirect:/menu"; // Default redirect ke halaman menu setelah menambah
    }

    /**
     * ✅ Endpoint baru: Langsung checkout satu item dari halaman detail menu.
     * Ini akan membersihkan keranjang yang ada dan hanya menambahkan item yang dipilih.
     * @param menuId ID menu yang akan di-checkout.
     * @param jumlah Jumlah menu.
     * @param cart Objek keranjang dari sesi.
     * @return String redirect URL ke halaman checkout.
     */
    @PostMapping("/cart/checkout-direct")
    public String checkoutDirect(@RequestParam Long menuId,
                                 @RequestParam(defaultValue = "1") int jumlah,
                                 @ModelAttribute("cart") List<CartItem> cart) {

        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null) {
            System.out.println("Menu dengan ID " + menuId + " tidak ditemukan untuk direct checkout.");
            return "redirect:/menu"; // Redirect ke halaman menu jika menu tidak valid
        }

        // ✅ Kunci: Kosongkan keranjang saat ini untuk direct checkout
        cart.clear();
        System.out.println("Keranjang dikosongkan untuk direct checkout.");

        // ✅ Tambahkan hanya item yang dipilih
        cart.add(new CartItem(menu, jumlah));
        System.out.println("Item '" + menu.getNama() + "' (" + jumlah + ") ditambahkan untuk direct checkout.");

        return "redirect:/cart/checkout"; // Langsung redirect ke halaman checkout
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
        boolean removed = cart.removeIf(item -> item.getMenu().getId().equals(menuId));
        if (removed) {
            System.out.println("Item dengan ID " + menuId + " berhasil dihapus dari keranjang.");
        } else {
            System.out.println("Item dengan ID " + menuId + " tidak ditemukan di keranjang.");
        }
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
                    System.out.println("Jumlah '" + item.getMenu().getNama() + "' diperbarui: " + item.getJumlah());
                } else if ("decrease".equals(action)) {
                    item.setJumlah(item.getJumlah() - 1);
                    System.out.println("Jumlah '" + item.getMenu().getNama() + "' diperbarui: " + item.getJumlah());
                    if (item.getJumlah() <= 0) {
                        cart.remove(i);
                        System.out.println("Item '" + item.getMenu().getNama() + "' dihapus karena jumlah <= 0.");
                        break;
                    }
                }
                break;
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart/checkout")
    public String showCheckoutForm(@ModelAttribute("cart") List<CartItem> cart, Model model) {
        if (cart.isEmpty()) {
            System.out.println("Keranjang kosong saat mencoba checkout.");
            return "redirect:/cart";
        }
        double total = cart.stream().mapToDouble(CartItem::getTotalHarga).sum();
        model.addAttribute("total", total);
        return "checkout";
    }

    @PostMapping("/cart/submit-checkout")
    @Transactional
    public String submitCheckout(@ModelAttribute("cart") List<CartItem> cart,
                                 @RequestParam String alamat,
                                 @RequestParam String metodePembayaran,
                                 Principal principal) {

        System.out.println("===> Memulai proses submitCheckout.");
        System.out.println("Alamat: " + alamat);
        System.out.println("Metode Pembayaran: " + metodePembayaran);

        if (principal == null) {
            System.out.println("===> User belum login saat submit checkout. Redirect ke halaman login.");
            return "redirect:/login";
        }

        if (cart == null || cart.isEmpty()) {
            System.out.println("===> Keranjang kosong saat submit checkout. Tidak dapat melakukan checkout.");
            return "redirect:/cart";
        }

        String username = principal.getName();
        System.out.println("===> Mencari User dengan username: " + username);
        Optional<User> userOptional = userRepository.findByUsername(username);

        User user = null;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }

        if (user == null) {
            System.err.println("===> ERROR: User tidak ditemukan di database untuk username: " + username + ". Redirect ke halaman error.");
            return "redirect:/error";
        }
        System.out.println("===> User ditemukan: ID=" + user.getId() + ", Username=" + user.getUsername());

        double total = cart.stream().mapToDouble(CartItem::getTotalHarga).sum();
        if (total <= 0) {
            System.out.println("===> ERROR: Total transaksi tidak valid (<= 0): " + total);
            return "redirect:/cart";
        }

        Transaksi transaksi = new Transaksi();
        transaksi.setTanggal(LocalDateTime.now());
        transaksi.setTotal(total);
        transaksi.setAlamat(alamat);
        transaksi.setMetodePembayaran(metodePembayaran);
        transaksi.setUser(user);

        System.out.println("===> Objek Transaksi sebelum disimpan: " + transaksi);
        if (transaksi.getUser() != null) {
            System.out.println("    User ID di Transaksi: " + transaksi.getUser().getId());
        }

        try {
            transaksi = transaksiRepository.save(transaksi);
            System.out.println("===> Transaksi berhasil disimpan dengan ID: " + transaksi.getId());
        } catch (Exception e) {
            System.err.println("===> ERROR saat menyimpan Transaksi: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/error";
        }

        for (CartItem item : cart) {
            TransaksiMenu transaksiMenu = new TransaksiMenu();
            transaksiMenu.setMenu(item.getMenu());
            transaksiMenu.setJumlah(item.getJumlah());
            transaksiMenu.setTransaksi(transaksi);
            try {
                transaksiMenuRepository.save(transaksiMenu);
            } catch (Exception e) {
                System.err.println("===> ERROR saat menyimpan TransaksiMenu untuk menu " + item.getMenu().getNama() + ": " + e.getMessage());
                e.printStackTrace();
                return "redirect:/error";
            }
        }

        cart.clear();
        System.out.println("===> Checkout selesai, keranjang sudah dikosongkan.");

        return "redirect:/success";
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }
}