package com.ecommerce.wehackbackend.model.dto.response;


public record VenueResponseDto(
        Long id,
        String name,
        String location,
        Integer capacity,
        String description
) {}
