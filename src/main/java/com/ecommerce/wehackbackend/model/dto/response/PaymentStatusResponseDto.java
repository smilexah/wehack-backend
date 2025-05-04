package com.ecommerce.wehackbackend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentStatusResponseDto {
    private Long paymentId;
    private String status;
    private Instant paidAt;
    private String transactionReference;
    private String paymentMethod;
}