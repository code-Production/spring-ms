package com.geekbrains.springms.address.services;

import com.geekbrains.springms.address.entities.Address;

import java.util.HashMap;

public class AddressIdentityMap {
    private final HashMap<Long, Address> addresses;

    public AddressIdentityMap() {
        this.addresses = new HashMap<>();
    }

    public void addAddress(Address address) {
        addresses.put(address.getId(), address);
    }

    public Address findAddress(Long id) {
        return addresses.get(id);
    }

    public void deleteAddress(Long id) {
        addresses.remove(id);
    }
}
