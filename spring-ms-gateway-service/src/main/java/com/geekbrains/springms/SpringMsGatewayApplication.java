package com.geekbrains.springms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringMsGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMsGatewayApplication.class, args);
	}

}
