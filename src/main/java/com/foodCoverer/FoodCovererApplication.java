package com.foodCoverer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@EnableJpaRepositories("com.foodCoverer")
@EntityScan("com.foodCoverer")
public class FoodCovererApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodCovererApplication.class, args);
	}

}
