package com.geekbrains.springms.cart.integrations;

import com.geekbrains.springms.api.AppError;
import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.cart.models.Cart;
import com.geekbrains.springms.cart.services.CartServices;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class OrderServiceIntegration {

    private WebClient orderServiceWebClient;

    @Resource(name = "orderServiceWebClient")
    public void setOrderServiceWebClient(WebClient orderServiceWebClient) {
        this.orderServiceWebClient = orderServiceWebClient;
    }

    //todo route roles
    public OrderDto createOrderFromCartContent(CartDto cartDto, String username) {
        return orderServiceWebClient.post()
                .uri("/")
                .header("username", username)
                .bodyValue(cartDto)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(AppError.class)
                                .flatMap(appError -> Mono.error(new ResponseStatusException(
                                        HttpStatusCode.valueOf(Integer.parseInt(appError.getCode())),
                                        appError.getMessage()
                                ))))
                .bodyToMono(OrderDto.class)
                .block();
    }
}

