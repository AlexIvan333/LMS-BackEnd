package com.lms.assignment_service.validation.interfaces;


import com.lms.assignment_service.dtos.requests.CreateAssigmentSubmissionRequest;

public interface IAssigmentSubmissionValidation {
    boolean isValid(CreateAssigmentSubmissionRequest request);
}
