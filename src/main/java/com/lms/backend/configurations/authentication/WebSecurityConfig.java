package com.lms.backend.configurations.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                "/swagger-ui/**", // Allow access to Swagger UI
                                "/v3/api-docs/**", // Allow access to API docs
                                "/swagger-ui.html" // Allow access to Swagger HTML page
                        ).permitAll() // Public access to Swagger endpoints
                        .anyRequest().authenticated() // Secure all other endpoints
                )
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity
                .formLogin(AbstractHttpConfigurer::disable) // Disable login form
                .httpBasic(AbstractHttpConfigurer::disable); // Disable HTTP Basic Authentication

        return http.build();
    }


}
