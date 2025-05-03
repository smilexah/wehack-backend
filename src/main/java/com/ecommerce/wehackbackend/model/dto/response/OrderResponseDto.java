package com.ecommerce.wehackbackend.model.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponseDto {
    private Long id;
    private Long eventId;
    private Integer quantity;
    private Double totalPrice;
    private String currency;
    private String status;
    private LocalDateTime createdAt;
}
