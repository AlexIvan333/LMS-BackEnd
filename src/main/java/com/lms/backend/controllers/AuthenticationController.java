package com.lms.backend.controllers;

import com.lms.backend.dtos.requests.LoginRequest;
import com.lms.backend.dtos.requests.TwoFactorRequest;
import com.lms.backend.dtos.responses.LoginResponse;
import com.lms.backend.exceptions.InvalidCredentialsException;
import com.lms.backend.exceptions.TwoFactorAuthenticationException;
import com.lms.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = authService.login(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(LoginResponse.builder().message(e.getMessage()).build());
        }
    }

    @PostMapping("/2fa")
    public ResponseEntity<String> verify2FA(@RequestBody TwoFactorRequest request) {
        try {
            authService.verifyTwoFactorCode(request);
            return ResponseEntity.ok("Two-factor authentication successful.");
        } catch (TwoFactorAuthenticationException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}