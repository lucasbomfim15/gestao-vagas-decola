package com.example.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
@OpenAPIDefinition(
		info = @Info(
				title = "Gestão de Vagas",
				description = "API Responsável pela gestão de vagas de emprego.",
				version = "1"
		)
)
public class GestaoVagasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoVagasApplication.class, args);
	}

}
