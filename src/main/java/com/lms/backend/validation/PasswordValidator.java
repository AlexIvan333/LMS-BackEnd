package com.lms.backend.validation;

import com.lms.backend.validation.interfaces.IPasswordValidator;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements IPasswordValidator {
    @Override
    public boolean isValid(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            return false;
        }
        return true;
    }
}
