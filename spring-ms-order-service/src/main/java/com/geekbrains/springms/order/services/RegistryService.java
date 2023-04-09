package com.geekbrains.springms.order.services;

import org.springframework.stereotype.Component;


public class RegistryService {

    private static volatile RegistryService INSTANCE;
    private final OrderIdentityMap orderIdentityMap;

    private RegistryService() {
        orderIdentityMap = new OrderIdentityMap();
    }

    public static RegistryService getInstance() {
        if (INSTANCE == null) {
            synchronized (RegistryService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RegistryService();
                }
            }
        }
        return INSTANCE;
    }

    public OrderIdentityMap getOrderIdentityMap() {
        return orderIdentityMap;
    }

}
