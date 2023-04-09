package com.geekbrains.springms.order.integrations;


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
    public void setRedisTemplate(RedisTemplate<String, ProductDto> redisTemplate) {
        this.products = redisTemplate;
    }

    @Override
    public ProductDto findProductById(Long id) {
        String prefixedId = addPrefixTo(id.toString());
        if (Boolean.TRUE.equals(products.hasKey(prefixedId))) {
            return products.opsForValue().get(prefixedId);
        }
        ProductDto productDto = productServiceIntegration.findProductById(id);
        products.opsForValue().set(prefixedId, productDto);
        return productDto;
    }

    private String addPrefixTo(String str) {
        return RedisPrefix.PRODUCT.getPrefix() + str;
    }
}
