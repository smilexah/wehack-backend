package com.ecommerce.wehackbackend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentGatewayResponseDto {
    private String transactionId;
    private String paymentUrl;
    private String status;
}