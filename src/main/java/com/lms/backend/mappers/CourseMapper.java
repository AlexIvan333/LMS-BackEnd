package com.lms.backend.mappers;

import com.lms.backend.dtos.responses.CourseResponse;
import com.lms.backend.dtos.responses.CourseResponseForStudent;
import com.lms.backend.entities.relational.CourseEntity;

import java.util.stream.Collectors;

public class CourseMapper {
    public static CourseResponse ToCourseResponse(CourseEntity entity)
    {
        if (entity == null) return null;

        return CourseResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .modules(entity.getModules().stream().map(ModuleMapper :: toResponse).collect(Collectors.toList()))
                .students(entity.getEnrolledStudents().stream().map(StudentMapper :: toResponse).collect(Collectors.toList()))
                .instructorID(entity.getInstructor().getId())
                .maxStudents(entity.getMaxStudents())
                .build();
    }

    public static CourseResponseForStudent ToCourseResponseForStudents(CourseEntity entity)
    {
        if (entity == null) return null;

        return CourseResponseForStudent.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .modules(entity.getModules().stream().map(ModuleMapper :: toResponse).collect(Collectors.toList()))
                .instructorID(entity.getInstructor().getId())
                .maxStudents(entity.getMaxStudents())
                .build();
    }
}
