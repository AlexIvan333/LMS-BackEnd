package com.lms.backend.validation.interfaces;

import com.lms.backend.dtos.requests.CreateModuleRequest;

public interface IModuleValidator {
    boolean isValid(CreateModuleRequest request);
}
