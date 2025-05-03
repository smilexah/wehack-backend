package com.ecommerce.wehackbackend.service;


import com.ecommerce.wehackbackend.mapper.EventMapper;
import com.ecommerce.wehackbackend.mapper.VenueMapper;
import com.ecommerce.wehackbackend.model.dto.request.VenueRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.EventResponseDto;
import com.ecommerce.wehackbackend.model.dto.response.VenueResponseDto;
import com.ecommerce.wehackbackend.model.entity.Venue;
import com.ecommerce.wehackbackend.repository.EventRepository;
import com.ecommerce.wehackbackend.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueService {

    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;
    private final VenueMapper venueMapper;
    private final EventMapper eventMapper;

    public List<VenueResponseDto> getAll() {
        return venueRepository.findAll()
                .stream()
                .map(venueMapper::toDto)
                .collect(Collectors.toList());
    }

    public VenueResponseDto getById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venue not found"));
        return venueMapper.toDto(venue);
    }

    public List<EventResponseDto> getEventsInVenue(Long venueId) {
        return eventMapper.toDtoList(eventRepository.findAllByVenueId(venueId));
    }

    @Transactional
    public VenueResponseDto create(VenueRequestDto dto) {
        Venue venue = venueMapper.toEntity(dto);
        return venueMapper.toDto(venueRepository.save(venue));
    }

    @Transactional
    public VenueResponseDto update(Long id, VenueRequestDto dto) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venue not found"));
        venueMapper.updateVenue(venue, dto);
        return venueMapper.toDto(venueRepository.save(venue));
    }

    @Transactional
    public void delete(Long id) {
        venueRepository.deleteById(id);
    }
}