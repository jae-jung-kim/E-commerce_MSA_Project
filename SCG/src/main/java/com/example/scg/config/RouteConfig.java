package com.example.scg.config;

import com.example.scg.filter.AuthorizationHeaderFilter;
import com.example.scg.filter.CustomFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    private final CustomFilter customFilter;
    private final AuthorizationHeaderFilter authorizationHeaderFilter;

    public RouteConfig(CustomFilter customFilter, AuthorizationHeaderFilter authorizationHeaderFilter) {
        this.customFilter = customFilter;
        this.authorizationHeaderFilter = authorizationHeaderFilter;
    }

    @Bean
    public RouteLocator ms1Route(RouteLocatorBuilder builder) {


        return builder.routes()
                .route("user", r -> r.path("/user/**","/login","/logout")
//                        .filters(f -> f.addRequestHeader("user-request","user-request-header")//key,value형태로 입력
//                                        .addResponseHeader("user-response","user-response-header"))
                        .uri("lb://USER")) //로드 밸런싱을 통해 user서버가 scale out 되어도 scale out 된 서버를 eureka에서 찾아서 접속한다.
                .route("user", r -> r.path("/logout")
                        .uri("lb://USER"))
                .route("order", r -> r.path("/order/**")
                        .filters(f -> f.filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config())))
                        .uri("lb://ORDER"))
                .route("product", r -> r.path("/product/**")
                        .uri("lb://PRODUCT"))
                .build();
    }
}