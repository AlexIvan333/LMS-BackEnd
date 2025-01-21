package com.lms.backend.validation;

import com.lms.backend.domain.enums.Role;
import com.lms.backend.dtos.requests.CreateUserRequest;
import com.lms.backend.repositories.relational.UserRepository;
import com.lms.backend.validation.interfaces.IUserValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserValidation implements IUserValidation {
    private final UserRepository userRepository;

    @Override
    public boolean HasValidEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean IsIdValid(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean IsInstructor(Long id) {
        if (userRepository.findById(id).get().getRole() == Role.INSTRUCTOR) {
            return true;
        };
        return false;
    }
}
