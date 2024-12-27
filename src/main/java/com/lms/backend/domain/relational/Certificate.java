package com.lms.backend.domain.relational;

import lombok.Data;

import java.util.Date;
@Data
public class Certificate {
    private Long id;
    private Long studentID;
    private Student student;
    private Long courseID;
    private Course course;
    private Date issueDate;
    private String certificateUrl;
}
