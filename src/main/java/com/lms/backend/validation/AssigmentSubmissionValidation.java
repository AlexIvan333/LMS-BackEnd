package com.lms.backend.validation;

import com.lms.backend.dtos.requests.CreateAssigmentSubmissionRequest;
import com.lms.backend.repositories.relational.AssignmentRepository;
import com.lms.backend.repositories.relational.StudentRepository;
import com.lms.backend.validation.interfaces.IAssigmentSubmissionValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AssigmentSubmissionValidation implements IAssigmentSubmissionValidation {
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;
    @Override
    public boolean isValid(CreateAssigmentSubmissionRequest request) {
        if(!studentRepository.existsById(request.getStudentId())) return false;
        return assignmentRepository.existsById(request.getAssigmentId());
    }
}
