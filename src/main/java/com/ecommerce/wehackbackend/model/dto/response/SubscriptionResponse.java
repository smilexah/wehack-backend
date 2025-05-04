package com.ecommerce.wehackbackend.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionResponse {
    private Long userId;
    private Long clubId;
    private String clubName;
    private LocalDateTime subscribedAt;
}