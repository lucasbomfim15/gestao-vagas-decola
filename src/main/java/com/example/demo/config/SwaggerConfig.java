package com.example.demo.config;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class SwaggerConfig {


    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info().title("Vacancy Management").description("API Responsible for managing job vacancies.").version("1.0"))
                .schemaRequirement("jwt_auth", createSecurityScheme());
    }


    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme().name("jwt_auth").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");
    }

}
