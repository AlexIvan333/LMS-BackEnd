package com.lms.backend.validation.interfaces;

import com.lms.backend.dtos.requests.CreateUserRequest;

public interface IUserValidation {
    boolean HasValidEmail(String email);
    boolean IsIdValid (Long id);
    boolean IsInstructor(Long id);
}
