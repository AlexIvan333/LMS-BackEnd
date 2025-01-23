package com.lms.backend.validation.interfaces;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


public interface IPasswordValidator {
    boolean isValid(String password);
}
