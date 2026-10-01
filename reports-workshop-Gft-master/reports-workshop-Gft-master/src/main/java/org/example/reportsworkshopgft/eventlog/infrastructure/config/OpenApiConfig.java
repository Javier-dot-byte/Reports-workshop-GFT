package org.example.reportsworkshopgft.eventlog.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Reporting API")
                                .version("1.0.0")
                                .description(
                                        "Endpoints for checking the system state, real time statics and order history."));
    }
}
