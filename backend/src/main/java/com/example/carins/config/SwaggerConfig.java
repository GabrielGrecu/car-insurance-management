package com.example.carins.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI carInsuranceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Insurance API")
                        .description("REST API for managing cars, owners, and insurance policies")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Car Insurance Project Documentation")
                        .url("https://github.com/alexandrugabrielgrecu/car-insurance"));
    }
}
