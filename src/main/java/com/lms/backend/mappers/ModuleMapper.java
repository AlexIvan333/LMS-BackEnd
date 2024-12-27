package com.lms.backend.mappers;

import com.lms.backend.dtos.responses.ModuleResponse;
import com.lms.backend.entities.relational.ModuleEntity;
import com.lms.backend.repositories.nosql.ResourceRepository;
import lombok.AllArgsConstructor;

import java.util.Collections;
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
                .resources(
                        entity.getResources() != null
                                ? entity.getResources().stream().map(ResourceMapper::toResponse).collect(Collectors.toList())
                                : Collections.emptyList()
                )
                .courseId(entity.getCourse().getId())
                .build();
    }
}