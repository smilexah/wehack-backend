package com.ecommerce.wehackbackend.controller;

import com.ecommerce.wehackbackend.model.dto.request.OrderRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.OrderResponseDto;
import com.ecommerce.wehackbackend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto request) {
        OrderResponseDto response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders() {
        List<OrderResponseDto> orders = orderService.getUserOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<OrderResponseDto> getOrderDetails(@PathVariable Long orderId) {
        OrderResponseDto order = orderService.getOrderDetails(orderId);
        return ResponseEntity.ok(order);
    }
}