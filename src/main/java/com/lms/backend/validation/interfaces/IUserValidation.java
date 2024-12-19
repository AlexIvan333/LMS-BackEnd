package com.lms.backend.validation.interfaces;

import com.lms.backend.controllers.requests.CreateUserRequest;

public interface IUserValidation {
    boolean HasValidEmail(CreateUserRequest request);
}
