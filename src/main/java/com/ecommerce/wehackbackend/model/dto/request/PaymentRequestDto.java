package com.ecommerce.wehackbackend.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PaymentRequestDto {
    @NotNull(message = "Order ID cannot be null")
    private Long orderId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @Positive(message = "Amount must be positive")
    @NotNull(message = "Amount cannot be null")
    private Double amount;

    @NotBlank(message = "Currency cannot be blank")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    private String currency;
}