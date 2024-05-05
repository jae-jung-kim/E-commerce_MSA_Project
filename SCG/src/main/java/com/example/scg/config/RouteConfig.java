package com.example.scg.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator ms1Route(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("user", r -> r.path("/user/**")
                        .uri("http://localhost:8001"))
                .route("order", r -> r.path("/order/**")
                        .uri("http://localhost:8002"))
                .route("product", r -> r.path("/product/**")
                        .uri("http://localhost:8003"))
                .build();
    }
}