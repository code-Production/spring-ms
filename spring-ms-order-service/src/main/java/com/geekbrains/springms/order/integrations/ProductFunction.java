package com.geekbrains.springms.order.integrations;

import com.geekbrains.springms.api.ProductDto;

public interface ProductFunction {
    ProductDto findProductById(Long id);
}
