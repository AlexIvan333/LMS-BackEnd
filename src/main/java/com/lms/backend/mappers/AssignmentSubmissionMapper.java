package com.lms.backend.mappers;

import com.lms.backend.dtos.responses.AssignmentSubmissionResponse;
import com.lms.backend.entities.relational.AssignmentSubmissionEntity;

import java.util.Collections;
import java.util.stream.Collectors;

public class AssignmentSubmissionMapper {
    public static AssignmentSubmissionResponse toResponse(AssignmentSubmissionEntity entity) {
        if (entity == null) {return null;}

        return AssignmentSubmissionResponse.builder()
                .id(entity.getId())
                .studentID(entity.getStudent().getId())
                .assignmentID(entity.getAssignment().getId())
                .submissionTime(entity.getSubmissionTime())
                .resources(
                        entity.getResources() != null
                                ? entity.getResources().stream().map(ResourceMapper::toResponse).collect(Collectors.toList())
                                : Collections.emptyList()
                )
                .grade(entity.getGrade())
                .completed(entity.getCompleted())
                .comment(entity.getComment())
                .build();
    }
}
