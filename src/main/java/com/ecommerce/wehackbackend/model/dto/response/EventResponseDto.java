package com.ecommerce.wehackbackend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private String time;
    private Boolean isOnline;
    private String streamingUrl;
    private Double price;
    private Integer capacity;
    private LocalDateTime createdAt;
    private String clubName;
    private String venueName;
}
