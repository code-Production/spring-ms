package com.geekbrains.springms.gateway.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    public Claims getClaimsFromToken(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey("secret_key")
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean checkIfTokenExpired(String token) throws ExpiredJwtException {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

}
