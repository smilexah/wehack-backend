package com.ecommerce.wehackbackend.mapper;

import com.ecommerce.wehackbackend.model.dto.response.EventResponseDto;
import com.ecommerce.wehackbackend.model.entity.Event;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    public EventResponseDto toDto(Event event) {
        return EventResponseDto.builder()
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

    public List<EventResponseDto> toDtoList(List<Event> events) {
        return events.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
