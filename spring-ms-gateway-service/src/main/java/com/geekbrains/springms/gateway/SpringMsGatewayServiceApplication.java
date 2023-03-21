package com.geekbrains.springms.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringMsGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMsGatewayServiceApplication.class, args);
	}

}
