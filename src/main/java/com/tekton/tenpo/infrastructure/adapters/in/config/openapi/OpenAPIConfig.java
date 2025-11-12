package com.tekton.tenpo.infrastructure.adapters.in.config.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tenpo Calculation API")
                        .version("1.0")
                        .description("API para cálculo de porcentajes")
                        .contact(new Contact()
                                .name("Tenpo")
                                .email("support@tenpo.com")));
    }
}