package com.geekbrains.springms.order.integrations;

import com.geekbrains.springms.api.AddressDto;
import com.geekbrains.springms.api.AppError;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class AddressServiceIntegration {


    @Resource(name = "addressServiceWebClient")
    private WebClient addressServiceWebClient;

    // TODO: 21.03.2023 relay roles headers
    public AddressDto getAddressById(Long id, String username) {
        return addressServiceWebClient.get()
                .uri("/" + id)
                .header("username", username)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(AppError.class)
                            .flatMap(appError -> Mono.error(new ResponseStatusException(
                                    HttpStatusCode.valueOf(Integer.parseInt(appError.getCode())),
                                    appError.getMessage()
                            )))
                )
                .bodyToMono(AddressDto.class)
                .block();
    }
}
