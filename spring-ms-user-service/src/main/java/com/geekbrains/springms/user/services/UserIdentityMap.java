package com.geekbrains.springms.user.services;

import com.geekbrains.springms.api.UserDto;
import com.geekbrains.springms.user.entities.User;

import java.util.HashMap;

public class UserIdentityMap {
    private final HashMap<String, UserDto> users;

    public UserIdentityMap() {
        users = new HashMap<>();
    }

    public void addUser(UserDto user) {
        users.put(user.getUsername(), user);
    }

    public UserDto findUser(String username) {
        return users.get(username);
    }

    public void deleteUser(String username) {
        users.remove(username);
    }

}
