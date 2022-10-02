package com.web.client.springclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SpringclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringclientApplication.class, args);
	}

}
