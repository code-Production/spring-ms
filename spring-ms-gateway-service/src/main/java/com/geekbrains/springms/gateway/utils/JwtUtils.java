package com.geekbrains.springms.gateway.utils;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Component
public class JwtUtils {
    public static List<String> getRolesFromJwtToken(Jwt jwt) {

        Map<String, Object> clients = jwt.getClaimAsMap("resource_access");
        Assert.notNull(clients, "Token doesn't have resource_access field.");

        Map<String, Object> gateway = (Map<String, Object>) clients.get("gateway");
        Assert.notNull(gateway, "Token doesn't have gateway field.");

        List<String> roles = (List<String>) gateway.get("roles");
        Assert.notNull(roles, "Token doesn't have roles.");

        return roles;
    }

}
