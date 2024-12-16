package com.lms.backend.mappers;

import com.lms.backend.controllers.responses.InstructorResponse;
import com.lms.backend.domain.relational.Instructor;
import com.lms.backend.entities.relational.InstructorEntity;

import java.util.stream.Collectors;

public class InstructorMapper {
    public static InstructorResponse toResponse(InstructorEntity entity) {
        if (entity == null) return null;

        return InstructorResponse.builder()
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
                .createdCourses(entity.getCreatedCourses().stream().map(CourseMapper :: ToCourseResponse).collect(Collectors.toList()))
                .build();
    }
}
