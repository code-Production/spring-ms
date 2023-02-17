package com.geekbrains.springms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;



@EnableConfigServer
@SpringBootApplication
@EnableDiscoveryClient
public class SpringMsConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMsConfigServerApplication.class, args);
	}

}
