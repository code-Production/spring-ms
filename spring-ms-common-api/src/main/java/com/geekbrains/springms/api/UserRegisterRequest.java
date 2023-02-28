package com.geekbrains.springms.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    private String email;
    private String username;
    private String password;
    private List<String> roles;

}
