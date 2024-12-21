package com.neet_question_api.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(scanBasePackages = "com.neet_question_api.api.modal.ApiLog")
@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
		System.out.println("Hello World");
		System.out.println("hello harsh");
	}
}
