package com.lms.backend.validation;

import com.lms.backend.controllers.requests.CreateUserRequest;
import com.lms.backend.repositories.relational.UserRepository;
import com.lms.backend.validation.interfaces.IUserValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@Component
public class UserValidation implements IUserValidation {
    private final UserRepository userRepository;

    @Override
    public boolean HasValidEmail(CreateUserRequest request) {
        return userRepository.findByEmail(request.getEmail()).isPresent();
    }
}
