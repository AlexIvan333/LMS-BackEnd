package com.lms.backend.validation;

import com.lms.backend.dtos.requests.CreateAssigmentRequest;

import com.lms.backend.repositories.nosql.ResourceRepository;
import com.lms.backend.repositories.relational.ModuleRepository;
import com.lms.backend.validation.interfaces.IAssigmentValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AssigmentValidation implements IAssigmentValidation {
    private final ModuleRepository moduleRepository;
    private final ResourceRepository resourceRepository;

    @Override
    public boolean isValid(CreateAssigmentRequest request) {
        if (!moduleRepository.existsById(request.getModuleId())) {
            return false;
        }
        return request.getResourceIds().stream().allMatch(resourceRepository::existsResourceById);
    }
}
