package com.lms.assignment_service.validation;


import com.lms.assignment_service.dtos.requests.CreateAssigmentSubmissionRequest;
import com.lms.assignment_service.repositories.AssignmentRepository;
import com.lms.assignment_service.repositories.AssignmentSubmissionRepository;
import com.lms.assignment_service.validation.interfaces.IAssigmentSubmissionValidation;
import com.lms.shared.events.CheckResourceExistsEvent;
import com.lms.shared.events.CheckUserExistsEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class AssigmentSubmissionValidation implements IAssigmentSubmissionValidation {
    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    public boolean isValid(CreateAssigmentSubmissionRequest request) {
        if (request.getStudentId() == null) return false;

        kafkaTemplate.send("user-validation-request",
                new CheckUserExistsEvent(request.getStudentId(), "STUDENT", UUID.randomUUID().toString()));

        if (!assignmentRepository.existsById(request.getAssigmentId())) return false;

        if (request.getResourceIds() != null && !request.getResourceIds().isEmpty()) {
            kafkaTemplate.send("resource-validation-request",
                    new CheckResourceExistsEvent(request.getResourceIds(), UUID.randomUUID().toString()));
        }

        return true;
    }

    @Override
    public boolean Exists(Long assigmentSubmissionId) {
        return assignmentSubmissionRepository.existsById(assigmentSubmissionId);
    }


}
