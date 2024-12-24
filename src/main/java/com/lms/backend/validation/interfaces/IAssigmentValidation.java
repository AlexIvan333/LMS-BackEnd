package com.lms.backend.validation.interfaces;

import com.lms.backend.dtos.requests.CreateAssigmentRequest;

public interface IAssigmentValidation {
    boolean isValid(CreateAssigmentRequest request);
}
