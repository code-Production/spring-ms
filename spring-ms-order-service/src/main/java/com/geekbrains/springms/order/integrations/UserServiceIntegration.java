package com.geekbrains.springms.order.integrations;

import com.geekbrains.springms.api.AppError;
import com.geekbrains.springms.api.UserDto;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

@Component
public class UserServiceIntegration {

    @Resource(name = "userServiceWebClient")
    private WebClient userServiceWebClient;

    public Boolean checkIfBillingBelongsToUser(String username, Long billingId, String authorizedUsername) {
        return userServiceWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/check")
                        .queryParam("username", username)
                        .queryParam("billing_id", billingId)
                        .build())
                .header("username", authorizedUsername)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse
                                .bodyToMono(AppError.class)
                                .flatMap(appError -> Mono.error(new ResponseStatusException(
                                        HttpStatusCode.valueOf(Integer.parseInt(appError.getCode())),
                                        appError.getMessage()
                                )))
                )
                .bodyToMono(Boolean.class)
                .block();
    }
}
