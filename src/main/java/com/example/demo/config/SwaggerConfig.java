package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo")) // Replace with your package name
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Seller-Portal API Documentation",
                "Description of your API",
                "1.0",
                "Terms of service",
                new Contact("Isuru Bandara", "https://www.linkedin.com/in/isuru-bandara", "isurubandara247@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
