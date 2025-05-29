package com.ooproject.controller;

import com.ooproject.model.Customer;
import com.ooproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@RequestParam String nama,
                               @RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Password dan konfirmasi password tidak sama");
            return "register";  // Kembali ke halaman register
        }

        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username sudah digunakan");
            return "register";
        }

        Customer customer = new Customer();
        customer.setName(nama);
        customer.setEmail(email);
        customer.setUsername(username);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setRole("USER");

        userRepository.save(customer);

        return "redirect:/login";
    }
}