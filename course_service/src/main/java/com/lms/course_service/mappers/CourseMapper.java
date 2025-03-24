package com.lms.course_service.mappers;

import com.lms.course_service.dtos.responses.CourseResponse;
import com.lms.course_service.entities.CourseEntity;

import java.util.stream.Collectors;

public class CourseMapper {
    public static CourseResponse toResponse(CourseEntity entity) {
        if (entity == null) {
            return null;
        }
        return CourseResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .instructorID(entity.getInstructorId())
                .maxStudents(entity.getMaxStudents())
                .modules(entity.getModules().stream().map(ModuleMapper::toResponse).collect(Collectors.toList()))
                .build();

    }
}
