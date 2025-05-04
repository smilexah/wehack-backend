package com.ecommerce.wehackbackend.controller;

import com.ecommerce.wehackbackend.mapper.SubscriptionMapper;
import com.ecommerce.wehackbackend.model.dto.response.SubscriptionResponse;
import com.ecommerce.wehackbackend.model.entity.Subscription;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.service.SubscriptionService;
import com.ecommerce.wehackbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final SubscriptionMapper subscriptionMapper;

    @PostMapping("/{clubId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<SubscriptionResponse> subscribe(@PathVariable Long clubId) {
        User user = userService.getCurrentUser();
        Subscription subscription = subscriptionService.subscribe(user.getId(), clubId);
        return ResponseEntity.ok(subscriptionMapper.toDto(subscription));
    }

    @DeleteMapping("/{clubId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long clubId) {
        User user = userService.getCurrentUser();
        subscriptionService.unsubscribe(user.getId(), clubId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<SubscriptionResponse>> getMySubscriptions() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(
            subscriptionService.getUserSubscriptions(user.getId())
                .stream()
                .map(subscriptionMapper::toDto)
                .toList()
        );
    }
}