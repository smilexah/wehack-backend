package com.ecommerce.wehackbackend.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventRequestDto {
    @NotNull(message = "Club ID is required")
    private Long clubId;

    private Long venueId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    private String description;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date must be in the present or future")
    private LocalDate date;

    @NotBlank(message = "Time is required")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$",
            message = "Time must be in HH:mm format")
    private String time;

    private Boolean isOnline = false;
    private String streamingUrl;

    @PositiveOrZero(message = "Price must be positive or zero")
    private Double price;

    @Positive(message = "Capacity must be positive")
    private Integer capacity;
}
