package com.geekbrains.springms.gateway.filters;

import com.geekbrains.springms.gateway.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtTokenFilter extends AbstractGatewayFilterFactory<JwtTokenFilter.Config> {

    private JwtUtils jwtUtils;

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public JwtTokenFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();

                if (request.getHeaders().containsKey("username") || request.getHeaders().containsKey("roles")) {
                    return onError(exchange, "Forbidden header in request.", HttpStatus.BAD_REQUEST);
                }
                if (containsAuthHeader(request)) {
                    String token = getAuthToken(request);
                    try {
                        jwtUtils.checkIfTokenExpired(token);
                    } catch (ExpiredJwtException ex) {
                        return onError(exchange, "Authorization token is expired", HttpStatus.UNAUTHORIZED);
                    }
                    populateRequestWithHeaders(request, token);
                }

                return chain.filter(exchange);
            };
        };
    }

    public static class Config {
    }

    public Mono<Void> onError(ServerWebExchange exchange, String msg, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        DataBuffer buffer = response.bufferFactory().wrap(msg.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(buffer));
    }

    private boolean containsAuthHeader(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey("Authorization")) {
            return false;
        }
        if (!request.getHeaders().getOrEmpty("Authorization").get(0).startsWith("Bearer ")) {
            return false;
        }
        return true;
    }

    private String getAuthToken(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0).substring(7);
    }

    private void populateRequestWithHeaders(ServerHttpRequest request, String token) {
        Claims claims = jwtUtils.getClaimsFromToken(token);
        System.out.println("populating:" + claims.getSubject());
        request.mutate().header("username", claims.getSubject());
        List<String> authorities = (List<String>) claims.get("authorities");
        authorities.forEach(role -> request.mutate().header("roles", role));
    }


}
