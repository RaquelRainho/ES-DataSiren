package com.springKafka.datasiren;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories
@ComponentScan("com.springKafka")
public class datasirenApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(datasirenApplication.class, args);
		
	}
}
