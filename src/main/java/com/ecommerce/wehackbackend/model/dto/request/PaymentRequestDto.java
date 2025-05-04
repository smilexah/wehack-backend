package com.ecommerce.wehackbackend.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PaymentRequestDto {
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // "card", "wallet", etc.

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    private String currency;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @DecimalMax(value = "1000000.00", message = "Amount must be less than 1,000,000")
    private Double amount;

    @NotNull(message = "User ID is required")
    private Long userId;
}