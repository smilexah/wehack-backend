package com.ecommerce.wehackbackend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentResponseDto {
    private Long id;
    private Long orderId;
    private Long userId;
    private Double amount;
    private String currency;
    private String paymentMethod;
    private String status; // "pending", "success", "failed"
    private String paymentUrl; // For redirecting to payment gateway
    private LocalDateTime createdAt;
}