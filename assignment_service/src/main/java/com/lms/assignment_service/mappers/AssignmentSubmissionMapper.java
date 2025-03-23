package com.lms.assignment_service.mappers;

import com.lms.assignment_service.dtos.responses.AssignmentSubmissionResponse;
import com.lms.assignment_service.entities.AssignmentSubmissionEntity;

import java.util.Collections;

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
                .resources(Collections.emptyList()) // todo: get the list of the resources response from the resource service
                .build();
    }
}
