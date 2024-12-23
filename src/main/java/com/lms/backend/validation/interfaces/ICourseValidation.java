package com.lms.backend.validation.interfaces;

import com.lms.backend.domain.relational.Course;
import com.lms.backend.dtos.requests.CreateCourseRequest;

public interface ICourseValidation {
    boolean validateCourse(CreateCourseRequest request);
}
