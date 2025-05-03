package com.ecommerce.wehackbackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "telegram.bots")
@Getter
@Setter
public class TelegramBotProperties {
    private String botUsername;
    private String botToken;
}