package com.ecommerce.wehackbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@OpenAPIDefinition(
        info = @Info(
                title = "WeHack Backend API",
                version = "1.0",
                description = "Documentation for WeHack Backend REST API"
        )
)
public class WehackBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WehackBackendApplication.class, args);
    }

}
