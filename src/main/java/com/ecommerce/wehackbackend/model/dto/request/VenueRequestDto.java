package com.ecommerce.wehackbackend.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VenueRequestDto(
        @NotBlank String name,
        String location,
        @NotNull Integer capacity,
        String description
) {}
