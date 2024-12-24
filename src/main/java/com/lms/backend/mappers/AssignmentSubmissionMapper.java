package com.lms.backend.mappers;

import com.lms.backend.dtos.responses.AssignmentSubmissionResponse;
import com.lms.backend.entities.relational.AssignmentSubmissionEntity;

public class AssignmentSubmissionMapper {
    public static AssignmentSubmissionResponse toResponse(AssignmentSubmissionEntity entity) {
        if (entity == null) {return null;}

        return AssignmentSubmissionResponse.builder()
                .id(entity.getId())
                .studentID(entity.getStudent().getId())
                .assignmentID(entity.getAssignment().getId())
                .submissionTime(entity.getSubmissionTime())
                .resourceIds(entity.getResourceIds())
                .grade(entity.getGrade())
                .completed(entity.getCompleted())
                .comment(entity.getComment())
                .build();
    }
}
