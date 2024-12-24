package com.lms.backend.validation;

import com.lms.backend.dtos.requests.CreateAssigmentRequest;
import com.lms.backend.repositories.relational.AssignmentRepository;
import com.lms.backend.repositories.relational.ModuleRepository;
import com.lms.backend.validation.interfaces.IAssigmentValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AssigmentValidation implements IAssigmentValidation {
    private final ModuleRepository moduleRepository;
    @Override
    public boolean isValid(CreateAssigmentRequest request) {
        return moduleRepository.existsById(request.getModuleId());
    }
}
