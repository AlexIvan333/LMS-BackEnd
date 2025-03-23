package com.lms.assignment_service.mappers;



import com.lms.assignment_service.dtos.responses.AssignmentResponse;
import com.lms.assignment_service.entities.AssignmentEntity;

import java.util.Collections;

public class AssignmentMapper {
    public static AssignmentResponse toResponse(AssignmentEntity entity)
    {
        if (entity == null) return null;

        return AssignmentResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .deadline(entity.getDeadline())
                .courseID(entity.getCourseId())
                .moduleID(entity.getModule_id())
                .resources(Collections.emptyList()) // todo: get the list of the resources response from the resource service
                .build();
    }
}
