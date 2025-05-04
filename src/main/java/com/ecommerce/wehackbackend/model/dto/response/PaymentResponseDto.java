package com.ecommerce.wehackbackend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentResponseDto {
    private Long paymentId;
    private String clientSecret;
    private String status;
    private String transactionReference;
    private Instant createdAt;
//    private Long id;
//    private Long orderId;
//    private Long userId;
//    private Double amount;
//    private String currency;
//    private String paymentMethod;
//    private String status;
//    private String transactionReference;
//    private LocalDateTime paidAt;
//    private String receiptUrl; // For Stripe receipts
}