package com.ehdndqls.shuttle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShuttleApplication {
	public static void main(String[] args) {

		SpringApplication.run(ShuttleApplication.class, args);
	}

}
