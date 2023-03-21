package com.geekbrains.springms.gateway.filters;

import com.geekbrains.springms.gateway.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class AddHeadersFilter implements WebFilter {

    private JwtDecoder jwtDecoder;

    @Autowired
    public void setJwtDecoder(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;

    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Jwt jwt = jwtDecoder.decode(token);
                String username = (String) jwt.getClaims().get("preferred_username");
                Assert.notNull(username, "Token doesn't have preferred_username.");

                ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();

                requestBuilder.header("username", username);

                List<String> roles = JwtUtils.getRolesFromJwtToken(jwt);
                requestBuilder.headers(httpHeaders -> httpHeaders.addAll("roles", roles));

//                requestBuilder.header("token", token); //forward token

                return chain.filter(
                        exchange.mutate().request(
                                requestBuilder.build()
                        ).build()
                );

            } catch (JwtException ex) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad token");
            }
        }

        return chain.filter(exchange);

    }


}
