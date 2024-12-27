package com.lms.backend.services;

import com.lms.backend.exceptions.TwoFactorAuthenticationException;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

@Service
public class Google2FAService {
    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

    public String generateSecretKey() {
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        return key.getKey();
    }

    public boolean verifyTwoFactorCode(String secretKey, int code) {
        return googleAuthenticator.authorize(secretKey, code);
    }
}
