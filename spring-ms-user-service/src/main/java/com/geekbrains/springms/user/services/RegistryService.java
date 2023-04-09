package com.geekbrains.springms.user.services;

import jakarta.persistence.criteria.CriteriaBuilder;

public class RegistryService {
    private static volatile RegistryService INSTANCE;
    private final UserIdentityMap USER_IDENTITY_MAP;

    private RegistryService() {
        USER_IDENTITY_MAP = new UserIdentityMap();
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

    public UserIdentityMap getUserIdentityMap() {
        return USER_IDENTITY_MAP;
    }
}
