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
                        .uri("lb://USER")) //로드 밸런싱을 통해 user서버가 scale out 되어도 scale out 된 서버를 eureka에서 찾아서 접속한다.
                .route("order", r -> r.path("/order/**")
                        .uri("lb://ORDER"))
                .route("product", r -> r.path("/product/**")
                        .uri("lb://PRODUCT"))
                .build();
    }
}