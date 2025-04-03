package com.lms.assignment_service.mappers;



import com.lms.assignment_service.dtos.responses.AssignmentResponse;
import com.lms.assignment_service.entities.AssignmentEntity;
import com.lms.assignment_service.kafka.ResourceRequestDispatcher;
import com.lms.shared.dtos.ResourceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AssignmentMapper {
    private static ResourceRequestDispatcher dispatcher;
    public static AssignmentResponse toResponse(AssignmentEntity entity)
    {
        if (entity == null) return null;

        List<Long> resourceIds = entity.getResourceIds();
        String correlationId = dispatcher.requestResources(resourceIds);
        List<ResourceResponse> resources = dispatcher.getResponseIfAvailable(correlationId);

        return AssignmentResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .deadline(entity.getDeadline())
                .courseID(entity.getCourseId())
                .moduleID(entity.getModule_id())
                .resources(resources)
                .build();
    }
}
