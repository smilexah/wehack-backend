package com.ecommerce.wehackbackend.controller;

import com.ecommerce.wehackbackend.model.dto.request.ClubRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.ClubResponseDto;
import com.ecommerce.wehackbackend.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubController {
    private final ClubService clubService;

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<List<ClubResponseDto>> getAllClubs() {
        return ResponseEntity.ok(clubService.getAllClubs());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<ClubResponseDto> getClubById(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.getClubById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClubResponseDto> createClub(@RequestBody ClubRequestDto clubRequestDTO) {
        return ResponseEntity.ok(clubService.createClub(clubRequestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClubResponseDto> updateClub(
            @PathVariable Long id,
            @RequestBody ClubRequestDto clubRequestDTO) {
        return ResponseEntity.ok(clubService.updateClub(id, clubRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/events")
    @PreAuthorize("hasAnyRole('STUDENT', 'CLUB_MANAGER', 'ADMIN')")
    public ResponseEntity<List<ClubResponseDto.EventDTO>> getClubEvents(
            @PathVariable Long id) {
        return ResponseEntity.ok(clubService.getClubEvents(id));
    }

}
