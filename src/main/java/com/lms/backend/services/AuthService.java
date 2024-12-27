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


    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        // Generate a secret key if not already generated
        if (user.getTwoFactorSecretKey() == null || user.getTwoFactorSecretKey().isEmpty()) {
            String secretKey = google2FAService.generateSecretKey();
            user.setTwoFactorSecretKey(secretKey);
            userRepository.save(user);

            // Send the 2FA QR Code to the user
            String qrCodeUrl = "otpauth://totp/LMS:" + user.getEmail() + "?secret=" + secretKey + "&issuer=LMS";
            emailService.sendEmailWithQRCode(
                    user.getEmail(),
                    "Setup 2FA for Your LMS Account",
                    "Scan this QR Code to enable Two-Factor Authentication (2FA) on your account.",
                    qrCodeUrl
            );
        }

        // Generate and save a 6-digit 2FA code
        int twoFactorCode = new Random().nextInt(900000) + 100000;
        user.setTwoFactorCode(twoFactorCode);
        userRepository.save(user);

        // Send the 2FA code to the user's email
        emailService.sendEmail(
                user.getEmail(),
                "Your LMS 2FA Code",
                "Your Two-Factor Authentication code is: " + twoFactorCode
        );

        return LoginResponse.builder()
                .message("Two-factor authentication initiated. Please verify the code sent to your email.")
                .build();
    }

    public void verifyTwoFactorCode(TwoFactorRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new TwoFactorAuthenticationException("User not found."));

        if (user.getTwoFactorSecretKey() == null || user.getTwoFactorSecretKey().isEmpty()) {
            throw new TwoFactorAuthenticationException("Two-factor authentication not enabled for this user.");
        }

        // Verify the 2FA code
        boolean isValid = google2FAService.verifyTwoFactorCode(user.getTwoFactorSecretKey(), request.getCode());
        if (!isValid) {
            throw new TwoFactorAuthenticationException("Invalid two-factor authentication code.");
        }

        // Generate a JWT for successful login
        String token = jwtUtil.generateToken(user.getEmail());
        user.setTwoFactorCode(null); // Clear the 2FA code after successful verification
        userRepository.save(user);

        // Return the JWT
        LoginResponse.builder()
                .token(token)
                .message("Authentication successful.")
                .build();
    }

}
