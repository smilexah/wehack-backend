package com.ecommerce.wehackbackend.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.secret.key}")
    private String secretKey;

    @Value("${stripe.public.key}")
    private String publicKey;

    @PostConstruct
    public void initializeStripe() {
        Stripe.apiKey = secretKey;
    }

    @Bean
    public String stripePublicKey() {
        return publicKey;
    }
}