package com.ecommerce.wehackbackend.model.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TicketResponseDto {
    private Long id;
    private Long orderId;
    private Long userId;
    private Long eventId;
    private String qrCode;
    private String status;
    private LocalDateTime createdAt;
}