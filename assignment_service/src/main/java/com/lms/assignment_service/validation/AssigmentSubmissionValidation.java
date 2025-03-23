package com.lms.assignment_service.validation;


import com.lms.assignment_service.dtos.requests.CreateAssigmentSubmissionRequest;
import com.lms.assignment_service.repositories.AssignmentRepository;
import com.lms.assignment_service.validation.interfaces.IAssigmentSubmissionValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AssigmentSubmissionValidation implements IAssigmentSubmissionValidation {
    private final AssignmentRepository assignmentRepository;


    @Override
    public boolean isValid(CreateAssigmentSubmissionRequest request) {
        if (request.getStudentId() == null) {
            return false;  //todo: add check if the student exists in the system
        }

        if (!assignmentRepository.existsById(request.getAssigmentId())) {
            return false;
        }

        return true; //todo: add check if the resource ids exists in the system ;
    }
}
