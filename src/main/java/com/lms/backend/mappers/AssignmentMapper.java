package com.lms.backend.mappers;

import com.lms.backend.dtos.responses.AssignmentResponse;
import com.lms.backend.entities.relational.AssignmentEntity;

import java.util.Collections;
import java.util.stream.Collectors;

public class AssignmentMapper {
    public static AssignmentResponse toResponse(AssignmentEntity entity)
    {
        if (entity == null) return null;

        return AssignmentResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .deadline(entity.getDeadline())
                .moduleID(entity.getModule().getId())
                .resources(
                        entity.getResources() != null
                                ? entity.getResources().stream().map(ResourceMapper::toResponse).collect(Collectors.toList())
                                : Collections.emptyList()
                )
                .build();
    }
}
