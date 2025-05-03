package com.ecommerce.wehackbackend.controller;

import com.ecommerce.wehackbackend.model.dto.request.OrganizerRequestDto;
import com.ecommerce.wehackbackend.model.entity.OrganizerRequest;
import com.ecommerce.wehackbackend.service.OrganizerRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizer-requests")
@RequiredArgsConstructor
public class OrganizerRequestController {

    private final OrganizerRequestService organizerRequestService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> submitOrganizerRequest(@Valid @RequestBody OrganizerRequestDto dto) {
        organizerRequestService.submitRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrganizerRequest>> getAllRequests() {
        return ResponseEntity.ok(organizerRequestService.getAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRequestStatus(@PathVariable Long id, @RequestParam OrganizerRequest.Status status) {
        organizerRequestService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
