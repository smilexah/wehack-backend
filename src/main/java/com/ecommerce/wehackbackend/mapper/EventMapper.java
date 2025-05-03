package com.ecommerce.wehackbackend.mapper;

import com.ecommerce.wehackbackend.model.dto.request.EventRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.EventResponseDto;
import com.ecommerce.wehackbackend.model.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {
    Event toEntity(EventRequestDto dto);
    EventResponseDto toDto(Event entity);
    void updateEntity(EventRequestDto dto, @MappingTarget Event entity);

    EventResponseDto.TicketDTO ticketToDto(Ticket ticket);
    EventResponseDto.ReviewDTO reviewToDto(EventReview review);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    EventResponseDto.ClubDTO clubToDto(Club club);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "location", source = "location")
    EventResponseDto.VenueDTO venueToDto(Venue venue);
}