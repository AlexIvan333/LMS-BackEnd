package com.lms.auth_service.validation;

import com.lms.auth_service.entities.enums.Role;
import com.lms.auth_service.repositories.relational.UserRepository;
import com.lms.auth_service.validation.intrefaces.IUserValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserValidator implements IUserValidator {

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
        }
        return false;
    }
    @Override
    public boolean IsActive(String email)
    {
        return userRepository.findByEmail(email).get().getActive();
    }
}
