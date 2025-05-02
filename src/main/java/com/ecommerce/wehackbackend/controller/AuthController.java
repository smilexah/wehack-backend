package com.ecommerce.wehackbackend.controller;

import com.ecommerce.wehackbackend.model.dto.request.AuthRequestDto;
import com.ecommerce.wehackbackend.model.dto.request.RegisterRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.AuthResponseDto;
import com.ecommerce.wehackbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto requestDto) {
        return ResponseEntity.ok(authService.register(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refresh(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.refresh(token.substring(7)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token.substring(7));
        return ResponseEntity.ok().build();
    }
}
