package com.geekbrains.springms.gateway.configs;

import com.geekbrains.springms.gateway.utils.JwtUtils;
import jakarta.ws.rs.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public JwtDecoder getDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .cors().disable()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/address/countries/**").permitAll()
                .pathMatchers("/address/regions/**").permitAll()
                .pathMatchers("/address/cities/**").permitAll()
                .pathMatchers("/address/streets/**").permitAll()
                .pathMatchers("/address/**").authenticated()
                .pathMatchers("/cart/checkout/**").authenticated()
                .pathMatchers("/cart/**").permitAll()
                .pathMatchers("/order/**").authenticated()
                .pathMatchers(HttpMethod.GET,"/products/**").permitAll()
                .pathMatchers(HttpMethod.POST,"/products/**").hasAnyAuthority("role_admin")
                .pathMatchers(HttpMethod.PUT,"/products/**").hasAnyAuthority("role_admin")
                .pathMatchers(HttpMethod.DELETE,"/products/**").hasAnyAuthority("role_admin")
                .pathMatchers("/user/auth/**").permitAll()
                .pathMatchers("/user/register/**").permitAll()
                .pathMatchers("/user/check/**").permitAll()
                .pathMatchers("/user/**").authenticated()
                .pathMatchers("/scripts/**").permitAll()
                .anyExchange().permitAll()
                .and()
                .oauth2ResourceServer(customizer -> {
                    customizer.jwt(jwtCustomizer -> {
                        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

                        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                            List<String> roles = JwtUtils.getRolesFromJwtToken(jwt);

                            return roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .map(sga -> (GrantedAuthority) sga)
                                    .toList();
                        });
                        Converter<Jwt, Mono<AbstractAuthenticationToken>> converter =
                                new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);

                        jwtCustomizer.jwtAuthenticationConverter(converter);
                    });
                })
                .build();
    }


}
