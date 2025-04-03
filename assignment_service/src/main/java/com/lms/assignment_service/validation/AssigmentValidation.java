package com.lms.assignment_service.validation;


import com.lms.assignment_service.dtos.requests.CreateAssigmentRequest;
import com.lms.assignment_service.validation.interfaces.IAssigmentValidation;
import com.lms.shared.events.CheckResourceExistsEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class AssigmentValidation implements IAssigmentValidation {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    public boolean isValid(CreateAssigmentRequest request) {
        if (request.getModuleId() == null) return false;

        if (request.getResourceIds() != null && !request.getResourceIds().isEmpty()) {
            kafkaTemplate.send("resource-validation-request",
                    new CheckResourceExistsEvent(request.getResourceIds(), UUID.randomUUID().toString()));
        }

        return true;
    }
}
