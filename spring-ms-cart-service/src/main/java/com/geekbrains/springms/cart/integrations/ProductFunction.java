package com.geekbrains.springms.cart.integrations;

import com.geekbrains.springms.api.ProductDto;

public interface ProductFunction {
    ProductDto getProductById(Long id);
}
