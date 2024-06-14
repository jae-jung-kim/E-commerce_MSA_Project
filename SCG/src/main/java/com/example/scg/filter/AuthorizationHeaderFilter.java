package com.example.scg.filter;


import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        this.env = env;
    }

    public static class Config {

    }


    //login -> token -> users(with token) -> header(inlcude token)
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> { //exchange에서  request, response 얻음
            ServerHttpRequest request = exchange.getRequest();

            //헤더에 포함된 토큰정보 확인
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "no authorization  header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        });
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;

        try {
            subject = Jwts.parser().setSigningKey(env.getProperty("spring.jwt.secret"))
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
        } catch (Exception ex) {
            returnValue = false;
        }
        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }
        return returnValue;
    }

    // Mono, Flux -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse(); //기존 MVC를 사용하면 HTTP sublet이라는 response사용
        response.setStatusCode(httpStatus);

        log.info(err);
        return response.setComplete(); //Mono타입으로 return
    }
}
