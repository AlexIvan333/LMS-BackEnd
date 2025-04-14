package com.lms.assignment_service.validation.interfaces;


import com.lms.assignment_service.dtos.requests.CreateAssignmentRequest;

public interface IAssignmentValidation {
    boolean isValid(CreateAssignmentRequest request);
}
