package com.geekbrains.springms.order.integrations;

import com.geekbrains.springms.api.AppError;
import com.geekbrains.springms.api.ProductDto;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class ProductServiceIntegration implements ProductFunction {

    @Resource(name = "productServiceWebClient")
    private WebClient productServiceIntegration;

    @Override
    public ProductDto findProductById(Long id) {
        return productServiceIntegration.get()
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
