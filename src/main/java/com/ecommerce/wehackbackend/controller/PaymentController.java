package com.ecommerce.wehackbackend.controller;

import com.ecommerce.wehackbackend.model.dto.request.PaymentRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.PaymentResponseDto;
import com.ecommerce.wehackbackend.model.dto.response.PaymentStatusResponseDto;
import com.ecommerce.wehackbackend.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PaymentResponseDto> createPayment(
            @Valid @RequestBody PaymentRequestDto paymentRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().toString());
        }

        PaymentResponseDto response = paymentService.createPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/status")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PaymentStatusResponseDto> getPaymentStatus(@PathVariable Long id) {
        PaymentStatusResponseDto status = paymentService.getPaymentStatus(id);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/stripe-webhook")
    public ResponseEntity<Void> stripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        paymentService.handleStripeWebhook(payload, sigHeader);
        return ResponseEntity.ok().build();
    }
}
