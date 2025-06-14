package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class RatingServiceApplication {
	
	

	public static void main(String[] args) {
		SpringApplication.run(RatingServiceApplication.class, args);
	}

}
