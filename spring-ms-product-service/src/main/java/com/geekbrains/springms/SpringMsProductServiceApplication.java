package com.geekbrains.springms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringMsProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMsProductServiceApplication.class, args);
	}


}
