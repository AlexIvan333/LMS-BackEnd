package com.lms.backend.services;

import com.lms.backend.configurations.authentication.JwtUtil;
import com.lms.backend.dtos.requests.LoginRequest;
import com.lms.backend.dtos.requests.TwoFactorRequest;
import com.lms.backend.dtos.responses.LoginResponse;
import com.lms.backend.entities.relational.UserEntity;
import com.lms.backend.exceptions.InvalidCredentialsException;
import com.lms.backend.exceptions.TwoFactorAuthenticationException;
import com.lms.backend.repositories.relational.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final Google2FAService google2FAService;


    public String initiateLogin(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        // Redirect to 2FA input page (frontend handles the UI part)
        return "Two-factor authentication required.";
    }

    public LoginResponse verifyTwoFactorCode(TwoFactorRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new TwoFactorAuthenticationException("User not found."));

        boolean isValid = google2FAService.verifyTwoFactorCode(user.getTwoFactorSecretKey(), request.getCode());
        if (!isValid) {
            throw new TwoFactorAuthenticationException("Invalid two-factor authentication code.");
        }

        // Generate JWT token after successful 2FA verification
        String token = jwtUtil.generateToken(user.getEmail(),user.getRole().name());

        return LoginResponse.builder()
                .token(token)
                .message("Authentication successful.")
                .build();
    }

}
