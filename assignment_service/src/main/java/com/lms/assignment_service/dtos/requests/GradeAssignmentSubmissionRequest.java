package com.lms.assignment_service.dtos.requests;

import com.lms.assignment_service.entities.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradeAssignmentSubmissionRequest {
    public Long assignmentSubmissionId;
    public Boolean completed;
    public String comment;
    public Grade grade;
}
