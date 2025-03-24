package com.lms.assignment_service.validation.interfaces;


import com.lms.assignment_service.dtos.requests.CreateAssigmentRequest;

public interface IAssigmentValidation {
    boolean isValid(CreateAssigmentRequest request);
}
