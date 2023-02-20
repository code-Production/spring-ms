package com.geekbrains.springms.cart;

import com.geekbrains.springms.cart.services.DiscoveryService;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class SpringMsCartServiceConfig {

    private static final int TIMEOUT = 30000;

    @Bean(name = "productServiceWebClient")
    public WebClient getProductServiceWebClient(DiscoveryService discoveryService){
        HttpClient httpClient = HttpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                });

        String errMsg = "Product service or eureka service seems to be not working";
        String productServiceUrl = discoveryService.getServiceUrlByName("product-service")
                .orElseThrow(() -> new NotFoundException(errMsg));

        return WebClient
                .builder()
                .baseUrl(productServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean(name = "orderServiceWebClient")
    public WebClient getOrderServiceWebClient(DiscoveryService discoveryService){
        HttpClient httpClient = HttpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                });

        String errMsg = "Order service or eureka service seems to be not working";
        String orderServiceUrl = discoveryService.getServiceUrlByName("order-service")
                .orElseThrow(() -> new NotFoundException(errMsg));

        return WebClient
                .builder()
                .baseUrl(orderServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
