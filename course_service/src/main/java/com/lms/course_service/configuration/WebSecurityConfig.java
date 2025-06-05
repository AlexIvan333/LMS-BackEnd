package com.lms.course_service.configuration;

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

    private final JwtUtil jwtUtil;

    public WebSecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Swagger resources accessible to everyone
                        .requestMatchers(SWAGGER_UI_RESOURCES).permitAll()
                        .requestMatchers("/actuator/prometheus").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info", "/actuator").permitAll()
                        .requestMatchers("/actuator/traces").permitAll()
                        // Public endpoints (modify as needed)
                        .requestMatchers(HttpMethod.GET, "/courses").hasAnyAuthority("STUDENT", "ADMIN","INSTRUCTOR")
                        .requestMatchers(HttpMethod.GET, "/courses/by-student").hasAnyAuthority("STUDENT", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/courses").hasAnyAuthority("INSTRUCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/courses/assignstudent").hasAnyAuthority("STUDENT", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/courses/modules/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/courses/modules/**").hasAnyAuthority("INSTRUCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/courses/modules/**").hasAnyAuthority("INSTRUCTOR", "ADMIN")
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
