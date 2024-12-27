package com.lms.backend.validation;

import com.lms.backend.dtos.requests.CreateModuleRequest;
import com.lms.backend.repositories.nosql.ResourceRepository;
import com.lms.backend.repositories.relational.CourseRepository;
import com.lms.backend.repositories.relational.ModuleRepository;
import com.lms.backend.validation.interfaces.IModuleValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ModuleValidator implements IModuleValidator {

    private final CourseRepository courseRepository;
    private final ResourceRepository resourceRepository;

    @Override
    public boolean isValid(CreateModuleRequest request){
        if(!courseRepository.existsById(request.getCourseId()))return false;
        return request.getResourceIds().stream().allMatch(resourceRepository::existsResourceById);
    }

}
