package com.geekbrains.springms.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringMsOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMsOrderServiceApplication.class, args);
	}


}
