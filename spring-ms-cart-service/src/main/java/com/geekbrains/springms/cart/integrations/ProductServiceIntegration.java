package com.geekbrains.springms.cart.integrations;

import com.geekbrains.springms.api.ProductDto;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ProductServiceIntegration {

    private WebClient productServiceWebClient;


    @Resource(name = "productServiceWebClient")
    public void setProductServiceWebClient(WebClient productServiceWebClient) {
        this.productServiceWebClient = productServiceWebClient;
    }

    public ProductDto getProductById(Long id) {

        return productServiceWebClient.get()
                .uri("/" + id)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }
}
