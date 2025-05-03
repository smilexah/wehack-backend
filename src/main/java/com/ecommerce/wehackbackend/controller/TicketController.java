package com.ecommerce.wehackbackend.controller;

import com.ecommerce.wehackbackend.model.dto.response.TicketResponseDto;
import com.ecommerce.wehackbackend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public List<TicketResponseDto> getUserTickets() {
        return ticketService.getUserTickets();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public TicketResponseDto getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @GetMapping("/{id}/qr")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<byte[]> getTicketQrCode(@PathVariable Long id) {
        byte[] qrCodeImage = ticketService.getQrCodeImage(id);
        return ResponseEntity
                .ok()
                .header("Content-Type", "image/png")
                .body(qrCodeImage);
    }
}