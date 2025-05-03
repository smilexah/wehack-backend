package com.ecommerce.wehackbackend.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketRequestDto {
    @NotNull
    private Long orderId;
    @NotNull
    private Long eventId;
}