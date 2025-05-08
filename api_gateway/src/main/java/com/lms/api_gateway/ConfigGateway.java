package com.lms.api_gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConfigGateway {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authservice", r -> r.path("/auth/**")
                        .uri("http://auth_service:8091"))
                .route("courseservice", r -> r.path("/courses/**")
                        .uri("http://course_service:8092"))
                .route("assignmentservice", r -> r.path("/assignments/**")
                        .uri("http://assignment_service:8093"))

                .route("resourceservice", r -> r.path("/resources/**")
                        .uri("http://resource_service:8094"))
                .build();
    }

}
