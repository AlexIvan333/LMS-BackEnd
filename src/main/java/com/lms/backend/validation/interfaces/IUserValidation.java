package com.lms.backend.validation.interfaces;

import com.lms.backend.dtos.requests.CreateUserRequest;

public interface IUserValidation {
    boolean HasValidEmail(CreateUserRequest request);
    boolean IsIdValid (Long id);
    boolean IsInstructor(Long id);
}
