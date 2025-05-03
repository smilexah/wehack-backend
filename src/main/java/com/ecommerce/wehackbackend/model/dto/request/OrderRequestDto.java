package com.ecommerce.wehackbackend.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderRequestDto {
    @NotNull
    private Long eventId;
    @Min(1) private Integer quantity;
    @NotBlank private String currency;
}