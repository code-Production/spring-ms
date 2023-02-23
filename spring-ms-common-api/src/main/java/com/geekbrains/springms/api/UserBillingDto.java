package com.geekbrains.springms.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserBillingDto {
    private Long id;
    private String cardNumber;
    private String holderName;
    private String expirationDate;
    private String cvvCode;
}
