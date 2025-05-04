package com.ecommerce.wehackbackend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentGatewayStatusDto {
    private String transactionId;
    private String status;
    private String receiptUrl;
}