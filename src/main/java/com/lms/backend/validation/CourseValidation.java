package com.lms.backend.validation;

import com.lms.backend.dtos.requests.CreateCourseRequest;
import com.lms.backend.repositories.relational.InstructorRepository;
import com.lms.backend.validation.interfaces.ICourseValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CourseValidation implements ICourseValidation {
    private final InstructorRepository instructorRepository;
    @Override
    public boolean validateCourse(CreateCourseRequest request) {
        if (!instructorRepository.existsById(request.getInstructorID())) return false;
        return request.getMaxStudents() > 0;
    }
}
