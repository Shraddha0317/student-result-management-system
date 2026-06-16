package com.shradha.student_result_management_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Result Management System API")
                        .description(
                                "REST API for managing students, subjects, " +
                                        "marks, results and CSV bulk uploads. " +
                                        "Built with Spring Boot, MySQL, JPA."
                        )
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Shradha")
                                .email("shradha@example.com")
                        )
                        .license(new License()
                                .name("MIT License")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ));
    }
}