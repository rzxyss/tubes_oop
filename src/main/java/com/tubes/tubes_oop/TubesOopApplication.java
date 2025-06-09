package com.tubes.tubes_oop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.tubes.repository")
@EntityScan("com.tubes.model")
@ComponentScan(basePackages = {
		"com.tubes.config",
		"com.tubes.Services",
		"com.tubes.controller",
		"com.tubes.model",
		"com.tubes.repository",
})
public class TubesOopApplication {

	public static void main(String[] args) {
		SpringApplication.run(TubesOopApplication.class, args);
	}

}
