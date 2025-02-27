package com.lms.auth_service.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TwoFactorAuthenticationException extends RuntimeException {
    public TwoFactorAuthenticationException(String message) {
        super(message);
    }

}
