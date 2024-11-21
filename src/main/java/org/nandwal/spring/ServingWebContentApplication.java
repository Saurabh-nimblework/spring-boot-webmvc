package org.nandwal.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServingWebContentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServingWebContentApplication.class, args);
		System.out.println("Spring application started !!");
	}

}
