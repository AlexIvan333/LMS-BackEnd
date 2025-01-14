package com.lms.backend.configurations.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig {

    private static final String[] SWAGGER_UI_RESOURCES = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    };


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Swagger resources accessible to everyone
                        .requestMatchers(SWAGGER_UI_RESOURCES).permitAll()
                        // Public endpoints
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login/admin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/students/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/instructors/**").permitAll()
                        // Protected endpoints
//                        .requestMatchers(HttpMethod.GET, "/students/**").hasAnyRole("ADMIN", "INSTRUCTOR")
//                        .requestMatchers(HttpMethod.GET, "/instructors/**","courses/forstudents").hasAnyRole("ADMIN", "STUDENT")
//                        .requestMatchers(HttpMethod.GET,  "/modules/**", "/assignments/**", "/assignmentsubmissions/**","/resources/**").hasAnyRole("ADMIN", "STUDENT", "INSTRUCTOR")
//                        .requestMatchers(HttpMethod.POST, "/courses/**", "/modules/**", "/assignments/**").hasAnyRole("ADMIN", "INSTRUCTOR")
//                        .requestMatchers(HttpMethod.POST, "/assignmentsubmissions/**").hasAnyRole("ADMIN", "STUDENT")
//                        .requestMatchers(HttpMethod.POST, "/admins/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST,"/resources/**").hasAnyRole("ADMIN", "STUDENT", "INSTRUCTOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtRequestFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }


}
