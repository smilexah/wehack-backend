package com.ecommerce.wehackbackend.controller;

import com.ecommerce.wehackbackend.model.dto.request.EventRequestDto;
import com.ecommerce.wehackbackend.model.dto.request.EventReviewRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.EventResponseDto;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto eventRequestDTO) {
        return ResponseEntity.ok(eventService.createEvent(eventRequestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequestDto eventRequestDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tickets")
    @PreAuthorize("hasAnyRole('CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<List<EventResponseDto.TicketDTO>> getEventTickets(
            @PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventTickets(id));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<EventResponseDto.ReviewDTO>> getEventReviews(
            @PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventReviews(id));
    }

    @PostMapping("/{id}/reviews")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EventResponseDto.ReviewDTO> addEventReview(
            @PathVariable Long id,
            @RequestBody EventReviewRequestDto reviewRequestDTO,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(eventService.addEventReview(id, reviewRequestDTO, currentUser));
    }

    // TODO: Complete the check-in functionality
//    @PostMapping("/{id}/checkin")
//    @PreAuthorize("hasAnyRole('CLUB_MANAGER', 'ADMIN')")
//    public ResponseEntity<Void> checkInToEvent(
//            @PathVariable Long id,
//            @RequestParam String qrCode,
//            @AuthenticationPrincipal User checkedInBy) {
//        eventService.checkInToEvent(id, qrCode, checkedInBy);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/{id}/checkin")
    @PreAuthorize("hasAnyRole('CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<Void> checkInToEvent(
            @PathVariable Long id,
            @RequestParam String qrCode,
            @AuthenticationPrincipal User checkedInBy) {
        eventService.checkInToEvent(id, qrCode, checkedInBy);
        return ResponseEntity.ok().build();
    }
}