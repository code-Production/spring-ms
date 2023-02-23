package com.geekbrains.springms.address;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringMsAddressServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMsAddressServiceApplication.class, args);
	}


}
