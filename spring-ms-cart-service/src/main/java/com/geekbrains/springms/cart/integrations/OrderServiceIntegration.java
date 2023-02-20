package com.geekbrains.springms.cart.integrations;

import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.cart.models.Cart;
import com.geekbrains.springms.cart.services.CartServices;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OrderServiceIntegration {

    private WebClient orderServiceWebClient;

    @Resource(name = "orderServiceWebClient")
    public void setOrderServiceWebClient(WebClient orderServiceWebClient) {
        this.orderServiceWebClient = orderServiceWebClient;
    }

    public OrderDto createOrderFromCartContent(CartDto cartDto) {
        return orderServiceWebClient.post()
                .uri("/")
//                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cartDto)
                .retrieve()
                .bodyToMono(OrderDto.class)
                .block();
    }
}
