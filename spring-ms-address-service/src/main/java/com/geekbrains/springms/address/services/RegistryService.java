package com.geekbrains.springms.address.services;

public class RegistryService {

    private static volatile RegistryService INSTANCE;
    private final AddressIdentityMap addressIdentityMap;

    private RegistryService() {
        this.addressIdentityMap = new AddressIdentityMap();
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

    public AddressIdentityMap getAddressIdentityMap() {
        return addressIdentityMap;
    }
}
