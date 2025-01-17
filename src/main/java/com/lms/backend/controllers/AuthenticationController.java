package com.lms.backend.controllers;

import com.lms.backend.dtos.requests.LoginRequest;
import com.lms.backend.dtos.requests.TwoFactorRequest;
import com.lms.backend.dtos.responses.LoginResponse;
import com.lms.backend.exceptions.InvalidCredentialsException;
import com.lms.backend.exceptions.TwoFactorAuthenticationException;
import com.lms.backend.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
            String loginResult = authService.initiateLogin(loginRequest);
            if ("Two-factor authentication required.".equals(loginResult)) {
                return ResponseEntity.status(202).body(loginResult); // 202 Accepted
            }
//            String token = authService.initiateLogin(loginRequest);
//            Cookie cookie = new Cookie("token", token);
//            cookie.setHttpOnly(true);
//            cookie.setSecure(true);
//            cookie.setPath("/");
//            cookie.setMaxAge(1800);
//            response.addCookie(cookie);
            return ResponseEntity.ok(loginResult);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("login/admin")
    public ResponseEntity<String> loginAdmin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try
        {
            String token= authService.loginAdmin(loginRequest).getToken();
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(1800);
            response.addCookie(cookie);
            return ResponseEntity.ok(token);
        }
        catch(InvalidCredentialsException e)
        {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/2fa")
    public ResponseEntity<LoginResponse> verify2FA(@RequestBody TwoFactorRequest request, HttpServletResponse response) {
        try {
            LoginResponse loginResponse = authService.verifyTwoFactorCode(request);
            String token = loginResponse.getToken();
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(1800);
            response.addCookie(cookie);
            return ResponseEntity.ok(loginResponse);
        } catch (TwoFactorAuthenticationException e) {
            return ResponseEntity.status(401).body(LoginResponse.builder().message(e.getMessage()).build());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@CookieValue(name = "token", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body("Token is missing or invalid.");
        }

        boolean isValid = authService.validateToken(token);
        if (isValid) {
            return ResponseEntity.ok("Token is valid.");
        } else {
            return ResponseEntity.status(401).body("Token is invalid or expired.");
        }
    }
}