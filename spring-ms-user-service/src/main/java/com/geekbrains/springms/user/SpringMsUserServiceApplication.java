package com.geekbrains.springms.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class SpringMsUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMsUserServiceApplication.class, args);
	}

}
