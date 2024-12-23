package com.lms.backend.dtos.responses;

import com.lms.backend.domain.enums.Grade;
import com.lms.backend.domain.relational.Assignment;
import com.lms.backend.domain.relational.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentSubmissionResponse {
    private Long id;
    private Long studentID;
    private Long assignmentID;
    private Date submissionTime;
    private String fileUrl;
    private Grade grade;
}
