package com.lms.auth_service.controllers;
import com.lms.auth_service.dtos.requests.LoginRequest;
import com.lms.auth_service.dtos.requests.TwoFactorRequest;
import com.lms.auth_service.dtos.responses.LoginResponse;
import com.lms.auth_service.dtos.responses.UserInfoResponse;
import com.lms.auth_service.exceptions.InvalidCredentialsException;
import com.lms.auth_service.exceptions.TwoFactorAuthenticationException;
import com.lms.auth_service.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

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

    @PutMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email, @RequestParam String newPassword,@RequestParam String verificationCode) {
        var result = authService.forgotPassword(email, newPassword,verificationCode);

        if (!result.isSuccess()) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(), result.getMessageError()
            );
            problemDetail.setTitle("Error resetting password");
            return ResponseEntity.status(result.getHttpStatus()).body(problemDetail);
        }

        return ResponseEntity.ok("Password updated successfully.");
    }

    @PostMapping("/send_verification_code")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String email) {
       var result =  authService.sendPasswordResetEmailVerification(email);

        if (!result.isSuccess()) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(), result.getMessageError()
            );
            problemDetail.setTitle("Error sending the email with verification code");
            return ResponseEntity.status(result.getHttpStatus()).body(problemDetail);
        }
        return ResponseEntity.ok("Email sent successfully.");
    }
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getLoggedInUser(HttpServletRequest request) {
        String token = null;
        // Extract token from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                UserInfoResponse userInfo = authService.getUserInfoFromToken(token);
                return ResponseEntity.ok(userInfo);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/delete_account")
    public ResponseEntity<?> deleteAccount(@CookieValue(name = "token", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing.");
        }

        boolean success = authService.deactivateAccount(token);
        if (success) {
            return ResponseEntity.ok("Account has been deactivated.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @GetMapping("/export_data")
    public ResponseEntity<byte[]> exportUserData(@CookieValue(name = "token", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        byte[] csvData = authService.exportUserDataAsCSV(token);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"user_data.csv\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csvData);
    }

    @DeleteMapping("/right_to_be_forgotten")
    public ResponseEntity<?> rightToBeForgotten(@CookieValue(name = "token", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing.");
        }

        boolean success = authService.deleteUserData(token);
        if (success) {
            return ResponseEntity.ok("User data deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }


}