package com.geekbrains.springms.cart.integrations;

import com.geekbrains.springms.api.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.geekbrains.springms.api.RedisPrefix;

@Component
public class ProductServiceIntegrationCached implements ProductFunction {

    private ProductServiceIntegration productServiceIntegration;

    private RedisTemplate<String, ProductDto> products;

    @Autowired
    public void setProductServiceIntegration(ProductServiceIntegration productServiceIntegration) {
        this.productServiceIntegration = productServiceIntegration;
    }

    @Autowired
    public void setProducts(RedisTemplate<String, ProductDto> products) {
        this.products = products;
    }

    @Override
    public ProductDto getProductById(Long id) {
        String prefixedId = addPrefixTo(id.toString());
        if (Boolean.TRUE.equals(products.hasKey(prefixedId))) {
            return products.opsForValue().get(prefixedId);
        }
        ProductDto productDto = productServiceIntegration.getProductById(id);
        products.opsForValue().set(prefixedId, productDto);
        return productDto;
    }

    private String addPrefixTo(String str) {
        return RedisPrefix.PRODUCT.getPrefix() + str;
    }
}
