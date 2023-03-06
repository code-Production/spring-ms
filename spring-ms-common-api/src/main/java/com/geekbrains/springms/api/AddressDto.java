package com.geekbrains.springms.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private Long id;
    private String username;
    private String country;
    private String region;
    private String city;
    private String street;
    private String houseNumber;
    private String apartmentNumber;

}
