package com.lms.backend.services;

import com.lms.backend.configurations.authentication.JwtUtil;
import com.lms.backend.dtos.requests.LoginRequest;
import com.lms.backend.dtos.requests.TwoFactorRequest;
import com.lms.backend.dtos.responses.LoginResponse;
import com.lms.backend.dtos.responses.ServiceResult;
import com.lms.backend.dtos.responses.StudentResponse;
import com.lms.backend.entities.relational.UserEntity;
import com.lms.backend.exceptions.InvalidCredentialsException;
import com.lms.backend.exceptions.TwoFactorAuthenticationException;
import com.lms.backend.repositories.relational.UserRepository;
import com.lms.backend.validation.interfaces.IPasswordValidator;
import com.lms.backend.validation.interfaces.IUserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final Google2FAService google2FAService;
    private final IUserValidation userValidation;
    private final IPasswordValidator passwordValidator;
    private final EmailService emailService;


    public String initiateLogin(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        // Redirect to 2FA input page (frontend handles the UI part)
        return "Two-factor authentication required.";
    }

    public LoginResponse loginAdmin(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        if (!"ADMIN".equals(user.getRole().toString())) {
            throw new InvalidCredentialsException("User is not an admin.");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());
        return LoginResponse.builder()
                .token(token)
                .message("Authentication successful.")
                .build();
    }

    public LoginResponse verifyTwoFactorCode(TwoFactorRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new TwoFactorAuthenticationException("User not found."));

        boolean isValid = google2FAService.verifyTwoFactorCode(user.getTwoFactorSecretKey(), request.getCode());
        if (!isValid) {
            throw new TwoFactorAuthenticationException("Invalid two-factor authentication code.");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return LoginResponse.builder()
                .token(token)
                .message("Authentication successful.")
                .build();
    }

    public boolean isAuthorized(String token, String requiredRole) {
        try {
            String email = jwtUtil.extractEmail(token);
            return jwtUtil.validateToken(token, email, requiredRole);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {

            boolean isValid = jwtUtil.validateToken(token, jwtUtil.extractEmail(token),jwtUtil.extractRole(token));

            boolean isExpired = jwtUtil.isTokenExpired(token);

            return isValid && !isExpired;
        } catch (Exception e) {
            System.err.println("Error during token validation: " + e.getMessage());
            return false;
        }
    }

    public ServiceResult<Boolean> sendPasswordResetEmailVerification(String email) {
        String verificationCode = emailService.sendVerificationCodeEmail(email);

        if(verificationCode == null) {
            return ServiceResult.<Boolean>builder()
                    .success(false)
                    .messageError("The email was not send")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
        if (userEntityOptional.isEmpty()) {
            return ServiceResult.<Boolean>builder()
                    .success(false)
                    .messageError("No account found with the provided email address.")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
        UserEntity userEntity = userEntityOptional.get();
        userEntity.setCodeForgotPassword(verificationCode);
        userRepository.save(userEntity);

        return ServiceResult.<Boolean>builder()
                .success(true)
                .data(true)
                .messageError("Email send successfully.")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ServiceResult<Boolean> forgotPassword(String email, String newPassword, String verificationCode) {

        if (!userValidation.HasValidEmail(email))
        {
            return ServiceResult.<Boolean>builder()
                    .success(false)
                    .messageError("There already is no account with this email address.")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        if (!passwordValidator.isValid(newPassword)) {
            return ServiceResult.<Boolean>builder()
                    .success(false)
                    .messageError("The password should match the requirements below")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
        if (userEntityOptional.isEmpty()) {
            return ServiceResult.<Boolean>builder()
                    .success(false)
                    .messageError("No account found with the provided email address.")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }


        UserEntity userEntity = userEntityOptional.get();

        if (!userEntity.getCodeForgotPassword().equals(verificationCode)) {
            return ServiceResult.<Boolean>builder()
                    .success(false)
                    .messageError("The verification code is incorrect.")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);

        return ServiceResult.<Boolean>builder()
                .success(true)
                .data(true)
                .messageError("Password updated successfully.")
                .httpStatus(HttpStatus.OK)
                .build();
    }





}
