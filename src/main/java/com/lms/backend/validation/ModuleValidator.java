package com.lms.backend.validation;

import com.lms.backend.dtos.requests.CreateModuleRequest;
import com.lms.backend.repositories.relational.CourseRepository;
import com.lms.backend.repositories.relational.ModuleRepository;
import com.lms.backend.validation.interfaces.IModuleValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ModuleValidator implements IModuleValidator {

    private final CourseRepository courseRepository;
    @Override
    public boolean isValid(CreateModuleRequest request){
        return courseRepository.existsById(request.getCourseId());
    }

}
