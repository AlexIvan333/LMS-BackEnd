package com.lms.course_service.mappers;

import com.lms.course_service.dtos.responses.ModuleResponse;
import com.lms.course_service.entities.ModuleEntity;


public class ModuleMapper {
    public static ModuleResponse toResponse(ModuleEntity entity) {
        if (entity == null) {
            return null;
        }


        return ModuleResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .courseId(entity.getCourse() != null ? entity.getCourse().getId() : null)
                .resources(entity.getResourceIds())
                .build();
    }
}
