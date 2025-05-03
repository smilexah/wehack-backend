package com.ecommerce.wehackbackend.mapper;

import com.ecommerce.wehackbackend.model.dto.response.EventResponseForVenueDto;
import com.ecommerce.wehackbackend.model.entity.Event;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapperForVenue {

    public EventResponseForVenueDto toDto(Event event) {
        return EventResponseForVenueDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .date(event.getDate())
                .time(event.getTime())
                .isOnline(event.getIsOnline())
                .streamingUrl(event.getStreamingUrl())
                .price(event.getPrice())
                .capacity(event.getCapacity())
                .createdAt(event.getCreatedAt())
                .clubName(event.getClub().getName())
                .venueName(event.getVenue() != null ? event.getVenue().getName() : null)
                .build();
    }

    public List<EventResponseForVenueDto> toDtoList(List<Event> events) {
        return events.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
