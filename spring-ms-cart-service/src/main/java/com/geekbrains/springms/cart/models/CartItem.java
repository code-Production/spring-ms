package com.geekbrains.springms.cart.models;

import com.geekbrains.springms.api.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private ProductDto productDto;
    private Integer amount;
    private BigDecimal sum;

}
