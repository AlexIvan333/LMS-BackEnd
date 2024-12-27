package com.lms.backend.validation.interfaces;

import com.lms.backend.dtos.requests.CreateAssigmentSubmissionRequest;

public interface IAssigmentSubmissionValidation {
    boolean isValid(CreateAssigmentSubmissionRequest request);
}
