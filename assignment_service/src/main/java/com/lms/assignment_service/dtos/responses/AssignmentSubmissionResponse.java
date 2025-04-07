package com.lms.assignment_service.dtos.responses;

import com.lms.assignment_service.entities.Grade;
import com.lms.shared.dtos.ResourceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentSubmissionResponse {
    private Long id;
    private Long studentID;
    private AssignmentResponse assignmentResponse;
    private Date submissionTime;
    private Grade grade;
    private Boolean completed;
    private String comment;
    private List<Long> resources;
}
