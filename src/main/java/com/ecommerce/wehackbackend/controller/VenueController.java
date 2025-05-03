package com.ecommerce.wehackbackend.controller;

import com.ecommerce.wehackbackend.model.dto.request.VenueRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.EventResponseForVenueDto;
import com.ecommerce.wehackbackend.model.dto.response.VenueResponseDto;
import com.ecommerce.wehackbackend.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    // ✅ MVP — доступны всем аутентифицированным (STUDENT, CLUB_MANAGER, ADMIN)
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'CLUB_MANAGER', 'ADMIN')")
    public List<VenueResponseDto> getAllHalls() {
        return venueService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<VenueResponseDto> getHall(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getById(id));
    }

    // ✅ CORE — доступно только ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VenueResponseDto> createHall(@Valid @RequestBody VenueRequestDto dto) {
        return ResponseEntity.ok(venueService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VenueResponseDto> updateHall(@PathVariable Long id, @Valid @RequestBody VenueRequestDto dto) {
        return ResponseEntity.ok(venueService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/events")
    @PreAuthorize("hasAnyRole('STUDENT', 'CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<List<EventResponseForVenueDto>> getEventsInHall(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getEventsInVenue(id));
    }

}
