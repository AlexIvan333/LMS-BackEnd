package com.lms.assignment_service.validation.interfaces;


import com.lms.assignment_service.dtos.requests.CreateAssignmentSubmissionRequest;

public interface IAssignmentSubmissionValidation {
    boolean isValid(CreateAssignmentSubmissionRequest request);
    boolean Exists(Long assigmentSubmissionId);
}
