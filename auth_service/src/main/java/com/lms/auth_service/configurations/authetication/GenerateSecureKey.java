package com.lms.auth_service.configurations.authetication;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerateSecureKey {
    public static void main(String[] args) {
        byte[] key = new byte[32]; // 256-bit key
        new SecureRandom().nextBytes(key);
        String base64Key = Base64.getEncoder().encodeToString(key);
        System.out.println("Base64 Secret Key: " + base64Key);
    }
}
