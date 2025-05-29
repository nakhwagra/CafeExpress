package com.ooproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ooproject.model.Customer;
import com.ooproject.repository.UserRepository;

@SpringBootApplication
public class CafeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeApplication.class, args);
	}

	@Bean
	public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				Customer admin = new Customer();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123")); // ← terenkripsi
				admin.setRole("ADMIN");
				admin.setName("Admin");
				admin.setEmail("admin@cafe.com");
				userRepository.save(admin);
				System.out.println("Admin account created!");
			}
		};
}


}
