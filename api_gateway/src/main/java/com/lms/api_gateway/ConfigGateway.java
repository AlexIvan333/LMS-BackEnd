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
                        .uri("http://authservice:8081")) //for docker
//                        .uri("http://localhost:8081")) // for local/intelliJ

                .route("courseservice", r -> r.path("/courses/**")
                        .uri("http://courseservice:8082"))
//                        .uri("http://localhost:8082")) // for local/intelliJ
                .route("assignmentservice", r -> r.path("/assignments/**")
                        .uri("http://assignmentservice:8083"))
//                        .uri("http://localhost:8082")
                .route("resourceservice", r -> r.path("/resources/**")
                        .uri("http://resourceservice:8084"))
//                        .uri("http://localhost:8082"))
                .build();
    }

}
