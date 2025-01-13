package com.example.compute_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ComputeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComputeServiceApplication.class, args);
	}

}
