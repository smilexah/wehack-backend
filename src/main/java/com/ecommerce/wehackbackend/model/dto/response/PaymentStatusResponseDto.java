package com.ecommerce.wehackbackend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusResponseDto {
    private String status;
    private String transactionId;
    private LocalDateTime processedAt;
    private String receiptUrl;
}