package com.geekbrains.springms.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class SpringMsAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMsAuthServiceApplication.class, args);
	}

}
