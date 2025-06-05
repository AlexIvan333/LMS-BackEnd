package com.lms.auth_service.configurations.authetication;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


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

    @Value("${app.allowed.origins:http://localhost:3001,https://localhost:3001,https://lmsapi.software}")
    private String allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Allow cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Swagger resources accessible to everyone
                        .requestMatchers(SWAGGER_UI_RESOURCES).permitAll()
                        .requestMatchers("/actuator/prometheus").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info", "/actuator").permitAll()
                        .requestMatchers("/actuator/traces").permitAll()
                        // Public endpoints
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login/admin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/send_verification_code").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/auth/forgot_password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/students/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/instructors/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/instructors/isvalidedbyadmin", "/auth/instructors/email").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/admins/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/me").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth/export_data/").hasAnyAuthority("ADMIN","STUDENT","INSTRUCTOR")
                        .requestMatchers(HttpMethod.GET,"/auth/delete_account/").hasAnyAuthority("ADMIN","STUDENT","INSTRUCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/auth/right_to_be_forgotten").hasAnyAuthority("ADMIN","STUDENT","INSTRUCTOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtRequestFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }



}
