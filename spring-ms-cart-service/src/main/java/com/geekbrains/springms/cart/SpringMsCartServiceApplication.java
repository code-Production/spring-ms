package com.geekbrains.springms.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class SpringMsCartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMsCartServiceApplication.class, args);
	}

}
