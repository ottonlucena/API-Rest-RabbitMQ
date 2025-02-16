package com.application.orderService.SpringBootRest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootRestOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestOrderApplication.class, args);
	}

}
