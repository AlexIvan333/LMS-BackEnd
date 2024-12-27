package com.lms.backend.dtos.responses;

import com.lms.backend.domain.enums.Grade;
import com.lms.backend.domain.relational.Assignment;
import com.lms.backend.domain.relational.Student;
import com.lms.backend.entities.nosql.ResourceEntity;
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
    private Long assignmentID;
    private Date submissionTime;
    private List<ResourceResponse> resources;
    private Grade grade;
    private Boolean completed;
    private String comment;
}
