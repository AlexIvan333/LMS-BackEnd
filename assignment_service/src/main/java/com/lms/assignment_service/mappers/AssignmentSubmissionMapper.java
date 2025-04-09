package com.lms.assignment_service.mappers;

import com.lms.assignment_service.dtos.responses.AssignmentSubmissionResponse;
import com.lms.assignment_service.entities.AssignmentSubmissionEntity;
import com.lms.assignment_service.kafka.ResourceRequestDispatcher;
import com.lms.shared.dtos.ResourceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AssignmentSubmissionMapper {
    public static AssignmentSubmissionResponse toResponse(AssignmentSubmissionEntity entity) {
        if (entity == null) {return null;}

        return AssignmentSubmissionResponse.builder()
                .id(entity.getId())
                .studentID(entity.getStudent_id())
                .assignmentResponse(AssignmentMapper.toResponse(entity.getAssignment()))
                .submissionTime(entity.getSubmission_time())
                .grade(entity.getGrade())
                .completed(entity.getCompleted())
                .comment(entity.getComment())
                .resources(entity.getResourceIds())
                .build();
    }
}
