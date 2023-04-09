package com.geekbrains.springms.cart.integrations;

import com.geekbrains.springms.api.AppError;
import com.geekbrains.springms.api.ProductDto;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class ProductServiceIntegration implements ProductFunction {

    private WebClient productServiceWebClient;


    @Resource(name = "productServiceWebClient")
    public void setProductServiceWebClient(WebClient productServiceWebClient) {
        this.productServiceWebClient = productServiceWebClient;
    }

    @Override
    public ProductDto getProductById(Long id) {

        return productServiceWebClient.get()
                .uri("/" + id)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(AppError.class)
                                .flatMap(appError -> Mono.error(new ResponseStatusException(
                                        HttpStatusCode.valueOf(Integer.parseInt(appError.getCode())),
                                        appError.getMessage()
                                ))))
                .bodyToMono(ProductDto.class)
                .block();
    }
}
