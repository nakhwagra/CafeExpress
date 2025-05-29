package com.ooproject.controller;

import com.ooproject.model.Menu;
import com.ooproject.model.Order;
import com.ooproject.repository.MenuRepository;
import com.ooproject.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/order")
    public String showOrderForm(Model model) {
        List<Menu> menus = menuRepository.findAll();
        model.addAttribute("menus", menus);
        return "order";
    }

    @PostMapping("/order")
    public String placeOrder(@RequestParam("menuId") Long menuId,
                             @RequestParam("jumlah") int jumlah,
                             @RequestParam("alamat") String alamat) {

        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null) {
            return "redirect:/order?error";
        }

        Order order = new Order();
        order.setMenu(menu);
        order.setJumlah(jumlah);
        order.setAlamat(alamat);
        order.setTotalHarga(menu.getHarga() * jumlah);
        orderRepository.save(order);

        return "redirect:/";
    }
}
