package com.ecommerce.wehackbackend;

import com.ecommerce.wehackbackend.config.TelegramBotProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TelegramBotProperties.class)
public class WehackBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WehackBackendApplication.class, args);
    }

}
