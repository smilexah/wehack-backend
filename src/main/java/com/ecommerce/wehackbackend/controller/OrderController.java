//package com.ecommerce.wehackbackend.controller;
//
//import com.ecommerce.wehackbackend.service.OrderService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("/api/orders")
//@RequiredArgsConstructor
//public class OrderController {
//
//    private final OrderService orderService;
//
//    @PostMapping
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto dto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(dto));
//    }
//
//    @GetMapping
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<List<OrderResponseDto>> getMyOrders() {
//        return ResponseEntity.ok(orderService.getCurrentUserOrders());
//    }
//
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
//    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
//        return ResponseEntity.ok(orderService.getOrderById(id));
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
//        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
//        orderService.cancelOrder(id);
//        return ResponseEntity.noContent().build();
//    }
//}