package com.geekbrains.springms.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProductDto {

    private Long id;
    private String title;
    private BigDecimal price;

}
