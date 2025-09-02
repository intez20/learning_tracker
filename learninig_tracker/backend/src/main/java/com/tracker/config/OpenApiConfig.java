package com.tracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI learningTrackerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Learning Tracker API")
                        .description("API for tracking learning progress across various subjects and projects")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
