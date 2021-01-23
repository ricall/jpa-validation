package io.ricall.jpa.demo.jpavalidation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class JpaValidationApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaValidationApplication.class, args);
	}

}
