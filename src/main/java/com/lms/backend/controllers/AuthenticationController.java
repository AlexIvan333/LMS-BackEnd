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
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            String response = authService.initiateLogin(loginRequest);
            return ResponseEntity.ok(response);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("login/admin")
    public ResponseEntity<String> loginAdmin(@RequestBody LoginRequest loginRequest) {
        String token= authService.loginAdmin(loginRequest).getToken();
        if ( token!= null) {
            return ResponseEntity.ok(token);
        }
        return null;
    }

    @PostMapping("/2fa")
    public ResponseEntity<LoginResponse> verify2FA(@RequestBody TwoFactorRequest request) {
        try {
            LoginResponse response = authService.verifyTwoFactorCode(request);
            return ResponseEntity.ok(response);
        } catch (TwoFactorAuthenticationException e) {
            return ResponseEntity.status(401).body(LoginResponse.builder().message(e.getMessage()).build());
        }
    }
}