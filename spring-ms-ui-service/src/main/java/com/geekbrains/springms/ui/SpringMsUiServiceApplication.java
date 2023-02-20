package com.geekbrains.springms.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringMsUiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMsUiServiceApplication.class, args);
	}

}
