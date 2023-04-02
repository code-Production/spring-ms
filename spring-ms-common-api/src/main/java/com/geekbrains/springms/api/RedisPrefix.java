package com.geekbrains.springms.api;

public enum RedisPrefix {

    CART("cart_"),
    PRODUCT("product_");

    private final String prefix;

    RedisPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
