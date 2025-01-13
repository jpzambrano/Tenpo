package com.example.compute_service.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  
        @Bean
        public OpenAPI customOpenAPI() {
            return new OpenAPI()
                    .openapi("3.0.0")  // Aquí especificas la versión correcta
                    .info(new Info()
                            .title("Compute Service API")
                            .description("API for dynamic percentage calculations and history management")
                            .version("1.0.0")
                            .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                    .externalDocs(new ExternalDocumentation()
                            .description("API Documentation")
                            .url("https://example.com/docs"));
        }
}
