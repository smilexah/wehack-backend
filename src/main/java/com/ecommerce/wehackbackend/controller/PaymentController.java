package com.ecommerce.wehackbackend.controller;

import com.ecommerce.wehackbackend.model.dto.request.PaymentRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.PaymentResponseDto;
import com.ecommerce.wehackbackend.model.dto.response.PaymentStatusResponseDto;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> createPayment(
            @Valid @RequestBody PaymentRequestDto paymentRequestDTO,
            @AuthenticationPrincipal User currentUser) {

        log.info("Received payment request: {}", paymentRequestDTO);

        // Check if user is authenticated
        if (currentUser == null) {
            log.error("Unauthenticated access attempt");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }

        // Validate user match
        if (!currentUser.getId().equals(paymentRequestDTO.getUserId())) {
            log.warn("User ID mismatch: auth={}, request={}",
                    currentUser.getId(), paymentRequestDTO.getUserId());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "User ID in request does not match authenticated user"));
        }

        try {
            PaymentResponseDto response = paymentService.initiatePayment(
                    paymentRequestDTO,
                    currentUser
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Payment processing failed", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Payment processing failed"));
        }
    }

    @GetMapping("/{id}/status")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PaymentStatusResponseDto> getPaymentStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        PaymentStatusResponseDto status = paymentService.getPaymentStatus(id, currentUser);
        return ResponseEntity.ok(status);
    }
}
