package com.geekbrains.springms.product.services;

import com.geekbrains.springms.api.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.geekbrains.springms.api.RedisPrefix;

@Component
public class ProductServiceCached implements ProductFunction {

    private ProductService productService;

    private RedisTemplate<String, ProductDto> products;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setProducts(RedisTemplate<String, ProductDto> products) {
        this.products = products;
    }

    @Override
    public ProductDto findProductById(Long id) {
        String prefixedId = addPrefixTo(id.toString());
        if (Boolean.TRUE.equals(products.hasKey(prefixedId))) {
            return products.opsForValue().get(prefixedId);
        }
        ProductDto productDto = productService.findProductById(id);
        products.opsForValue().set(prefixedId, productDto);
        return productDto;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        //clear redis cache for this product
        if (productDto.getId() != null) {
            products.delete(productDto.getId().toString());
        }
        return productService.updateProduct(productDto);
    }

    private String addPrefixTo(String str) {
        return RedisPrefix.PRODUCT.getPrefix() + str;
    }
}
