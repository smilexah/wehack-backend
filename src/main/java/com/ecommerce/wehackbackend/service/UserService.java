package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.config.TelegramBotProperties;
import com.ecommerce.wehackbackend.exception.ResourceNotFoundException;
import com.ecommerce.wehackbackend.mapper.UserMapper;
import com.ecommerce.wehackbackend.model.dto.response.UserResponseDto;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.repository.UserRepository;
import com.ecommerce.wehackbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TelegramBotProperties telegramBotProperties;

    public UserResponseDto getMe(String token) {
        String email = jwtUtil.extractUsername(token.substring(7));

        User user = userRepository.findByEmailAndIsActive(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return UserMapper.toDto(user);
    }

    @Transactional
    public String generateTelegramLink(String jwt) {
        String email = jwtUtil.extractUsername(jwt.substring(7));

        User user = userRepository.findByEmailAndIsActive(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        String token = UUID.randomUUID().toString();
        user.setTgLinkToken(token);
        user.setTgTokenCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        return "https://t.me/" + telegramBotProperties.getBotUsername() + "?start=" + token;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }

        String email = authentication.getName();
        return getUserByEmail(email);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmailAndIsActive(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

}
