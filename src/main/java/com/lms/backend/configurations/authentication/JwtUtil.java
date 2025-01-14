package com.lms.backend.configurations.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "Len/vFAFyqFMUYSp/9roemgQqyq2nnygvv3dO+IoTAc=";
    private static final SecretKey SECRET_KEY_SPEC = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(SECRET_KEY));// Use environment variables for production
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // No "ROLE_" prefix
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY_SPEC, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateToken(String token, String email, String requiredRole) {
        try {
            final String extractedEmail = extractEmail(token);
            final String extractedRole = extractRole(token);
            return extractedEmail.equals(email) && extractedRole.equals(requiredRole) && !isTokenExpired(token);
        } catch (Exception e) {
            return false; // Invalid token
        }
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY_SPEC)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
