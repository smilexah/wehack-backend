package com.ecommerce.wehackbackend.model.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventResponseDto {
    private Long id;
    private ClubDTO club;
    private VenueDTO venue;
    private String title;
    private String description;
    private LocalDate date;
    private String time;
    private Boolean isOnline;
    private String streamingUrl;
    private Double price;
    private Integer capacity;
    private Integer totalCapacity;
    private LocalDateTime createdAt;
    private List<TicketDTO> tickets;
    private List<ReviewDTO> reviews;

    @Data
    public static class ClubDTO {
        private Long id;
        private String name;
    }

    @Data
    public static class VenueDTO {
        private Long id;
        private String name;
        private String location;
    }

    @Data
    public static class TicketDTO {
        private Long id;
        private String qrCode;
        private String status;
    }

    @Data
    public static class ReviewDTO {
        private Long id;
        private Integer rating;
        private String comment;
        private UserDTO author;

        @Data
        public static class UserDTO {
            private Long id;
            private String username;
        }
    }
}