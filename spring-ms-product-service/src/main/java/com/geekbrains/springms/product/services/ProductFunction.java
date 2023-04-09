package com.geekbrains.springms.product.services;

import com.geekbrains.springms.api.ProductDto;
import com.geekbrains.springms.product.entities.Product;

import java.util.Optional;

public interface ProductFunction {
    ProductDto findProductById(Long id);
    ProductDto updateProduct(ProductDto productDto);
    void deleteProductById(Long id);
}
