package com.cpsp.MeBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.SQLException;

@SpringBootApplication
@EnableScheduling
public class MeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeBackendApplication.class, args);
    }

}
