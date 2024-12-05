package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt for password hashing
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for simplicity
            .authorizeHttpRequests()
                .requestMatchers("/api/users/register", "/api/users/login").permitAll() // Allow public access
                .requestMatchers("/api/trains/availability").permitAll() // Allow unauthenticated access to seat availability
                .requestMatchers("/api/trains/add").hasRole("ADMIN") // Restrict train addition to admin
                .requestMatchers("/api/bookings/**").authenticated() // Require authentication for bookings
                .anyRequest().authenticated() // Protect all other endpoints
            .and()
            .httpBasic(); // Basic HTTP authentication for simplicity

        return http.build();
    }

    // Expose AuthenticationManager as a bean (useful for custom authentication logic if needed)
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManager.class);
    }
}
