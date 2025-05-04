package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.exception.*;
import com.ecommerce.wehackbackend.model.dto.request.AuthRequestDto;
import com.ecommerce.wehackbackend.model.dto.request.RegisterRequestDto;
import com.ecommerce.wehackbackend.model.dto.request.SendVerificationCodeRequestDto;
import com.ecommerce.wehackbackend.model.dto.request.VerifyCodeRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.AuthResponseDto;
import com.ecommerce.wehackbackend.model.dto.response.RegisterResponseDto;
import com.ecommerce.wehackbackend.model.dto.response.TokenResponseDto;
import com.ecommerce.wehackbackend.model.entity.Token;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.repository.RoleRepository;
import com.ecommerce.wehackbackend.repository.TokenRepository;
import com.ecommerce.wehackbackend.repository.UserRepository;
import com.ecommerce.wehackbackend.util.JwtUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final TokenRepository tokenRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final JavaMailSender mailSender;

    private static final String VERIFICATION_EMAIL_SUBJECT = "Your Verification Code";
    private static final String VERIFICATION_EMAIL_BODY = "Your verification code is: ";

    public RegisterResponseDto register(RegisterRequestDto req) {
        userRepo.findByEmailAndIsActive(req.getEmail())
                .ifPresent(user -> {
                    throw new ResourceAlreadyExistsException("User", "email", req.getEmail());
                });

        var role = roleRepo.findByName("STUDENT")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "STUDENT"));

        var user = User.builder()
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .phoneNumber(req.getPhoneNumber())
                .isActive(true)
                .isVerified(false)
                .roles(Set.of(role))
                .build();

        userRepo.save(user);

        return RegisterResponseDto.builder()
                .email(user.getEmail())
                .build();
    }

    public AuthResponseDto login(AuthRequestDto req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        var user = userRepo.findByEmailAndIsActive(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", req.getEmail()));
        if (!user.getIsVerified()) {
            throw new InvalidCredentialsException("Please, verify your email");
        }
        return generateToken(user);
    }

    public AuthResponseDto refresh(String token) {
        var username = jwtUtil.extractUsername(token);

        var user = userRepo.findByEmailAndIsActive(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));

        return generateToken(user);
    }

    public void logout(String token) {
        var existing = tokenRepo.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "value", token));

        existing.setRevoked(true);
        tokenRepo.save(existing);
    }

    public void sendVerificationCode(SendVerificationCodeRequestDto req) {
        var user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", req.getEmail()));

        String code = generateVerificationCode();

        user.setVerificationCode(code);
        userRepo.save(user);

        sendVerificationEmail(user.getEmail(), code);
    }

    public AuthResponseDto verifyCode(VerifyCodeRequestDto req) {
        var user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", req.getEmail()));

        if (!user.getVerificationCode().equals(req.getCode())) {
            throw new InvalidCodeException("The verification code is invalid.");
        }

        user.setIsVerified(true);
        userRepo.save(user);

        return generateToken(user);
    }

    private String generateVerificationCode() {
        int code = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }

    private void sendVerificationEmail(String email, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Your Verification Code");
            helper.setText("Your verification code is: " + code);

            mailSender.send(message);
        } catch (Exception e) {
            throw new FailedToSendVerificationCode("Failed to send verification email");
        }
    }

    private AuthResponseDto generateToken(User user) {
        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        long expiration = jwtUtil.getAccessTokenExpirationMillis();

        tokenRepo.save(Token.builder()
                .token(accessToken)
                .expired(false)
                .revoked(false)
                .user(user)
                .build());

        tokenRepo.save(Token.builder()
                .token(refreshToken)
                .expired(false)
                .revoked(false)
                .user(user)
                .build());

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(expiration)
                .build();
    }
}

