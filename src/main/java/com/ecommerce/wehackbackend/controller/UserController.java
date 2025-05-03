package com.ecommerce.wehackbackend.controller;

import com.ecommerce.wehackbackend.model.dto.response.UserResponseDto;
import com.ecommerce.wehackbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getMe(token));
    }

    @GetMapping("/test")
    public ResponseEntity<Void> test() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/generate-telegram-token")
    public ResponseEntity<String> generateTelegramToken(@RequestHeader("Authorization") String token) {
        String telegramUrl = userService.generateTelegramLink(token);
        return ResponseEntity.ok(telegramUrl);
    }

}
