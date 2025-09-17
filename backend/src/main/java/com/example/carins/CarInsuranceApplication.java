package com.example.carins;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
        info = @Info(
                title = "Car Insurance API",
                version = "1.0.0",
                description = "API for managing car insurance policies, claims, and notifications"
        )
)
public class CarInsuranceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarInsuranceApplication.class, args);
    }
}