package com.ooproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**", "/about").permitAll()

            // Akses hanya untuk ADMIN
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/menu/add", "/menu/edit/**", "/menu/delete/**").hasRole("ADMIN")

            // Akses hanya untuk USER
            .requestMatchers("/menu/**","/order/**", "/cart", "/cart/**").hasRole("USER")

            // Bisa diakses oleh ADMIN & USER
            .requestMatchers("/menu", "/menu/**").hasAnyRole("USER", "ADMIN")

            // Semua permintaan lainnya harus login
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .successHandler(customLoginSuccessHandler())
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        );

    return http.build();
}


    @Bean
    public AuthenticationSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }
}
