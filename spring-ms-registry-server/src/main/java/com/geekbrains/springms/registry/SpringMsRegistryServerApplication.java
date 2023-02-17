package com.geekbrains.springms.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class SpringMsRegistryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMsRegistryServerApplication.class, args);
	}

}
