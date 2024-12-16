package com.lms.backend.mappers;

import com.lms.backend.controllers.responses.StudentResponse;
import com.lms.backend.domain.relational.Student;
import com.lms.backend.entities.relational.StudentEntity;

import java.util.stream.Collectors;

public class StudentMapper {
    public static StudentResponse toResponse(StudentEntity entity) {
        if (entity == null) return null;

        return StudentResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .middleName(entity.getMiddleName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .active(entity.getActive())
                .streetName(entity.getStreetName())
                .streetNumber(entity.getStreetNumber())
                .country(entity.getCountry())
                .city(entity.getCity())
                .zipCode(entity.getZipCode())
                .enrolledCourses(entity.getEnrolledCourses().stream().map(CourseMapper ::ToCourseResponseForStudents ).collect(Collectors.toList()))
                .build();
    }
}
