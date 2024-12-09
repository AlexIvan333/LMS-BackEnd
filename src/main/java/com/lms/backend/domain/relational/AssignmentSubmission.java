package com.lms.backend.domain.relational;

import com.lms.backend.domain.enums.Grade;
import lombok.Data;

import java.util.Date;
@Data
public class AssignmentSubmission {
    private Long id;
    private Long studentID;
    private Student student;
    private Long assignmentID;
    private Assignment assignment;
    private Date submissionTime;
    private String fileUrl;
    private Grade grade;
}
