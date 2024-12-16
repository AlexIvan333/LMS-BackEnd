package com.lms.backend.mappers;

import com.lms.backend.controllers.responses.ModuleResponse;
import com.lms.backend.entities.relational.ModuleEntity;

import java.util.stream.Collectors;

public class ModuleMapper {
    public static ModuleResponse toResponse(ModuleEntity entity) {
        if (entity == null) {
            return null;
        }

        return ModuleResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .assignments(entity.getAssignments().stream().map(AssignmentMapper::toResponse).collect(Collectors.toList()))
                .resourceIds(entity.getResourceIds().stream().map(Long::valueOf).collect(Collectors.toList()))
                .courseId(entity.getCourse().getId())
                .build();
    }
}