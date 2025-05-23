package com.lms.assignment_service.mappers;



import com.lms.assignment_service.dtos.responses.AssignmentResponse;
import com.lms.assignment_service.entities.AssignmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
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
                .resources(entity.getResourceIds())
                .build();
    }
}
