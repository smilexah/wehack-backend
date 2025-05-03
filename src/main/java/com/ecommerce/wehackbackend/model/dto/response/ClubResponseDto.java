package com.ecommerce.wehackbackend.model.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClubResponseDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<EventDTO> events;
    private List<ClubReviewDTO> reviews;
    private List<UserDTO> subscribers;

    @Data
    public static class EventDTO {
        private Long id;
        private String title;
        private LocalDate date;
    }

    @Data
    public static class ClubReviewDTO {
        private Long id;
        private Integer rating;
        private String comment;
    }

    @Data
    public static class UserDTO {
        private Long id;
        private String username;
    }
}
