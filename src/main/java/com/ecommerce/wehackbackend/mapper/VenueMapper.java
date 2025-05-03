package com.ecommerce.wehackbackend.mapper;

import com.ecommerce.wehackbackend.model.dto.request.VenueRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.VenueResponseDto;
import com.ecommerce.wehackbackend.model.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {

    public VenueResponseDto toDto(Venue venue) {
        return new VenueResponseDto(
                venue.getId(),
                venue.getName(),
                venue.getLocation(),
                venue.getCapacity(),
                venue.getDescription()
        );
    }

    public Venue toEntity(VenueRequestDto dto) {
        Venue venue = new Venue();
        venue.setName(dto.name());
        venue.setLocation(dto.location());
        venue.setCapacity(dto.capacity());
        venue.setDescription(dto.description());
        return venue;
    }

    public void updateVenue(Venue venue, VenueRequestDto dto) {
        venue.setName(dto.name());
        venue.setLocation(dto.location());
        venue.setCapacity(dto.capacity());
        venue.setDescription(dto.description());
    }
}