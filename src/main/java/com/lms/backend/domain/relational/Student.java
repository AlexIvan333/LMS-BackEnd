package com.lms.backend.domain.relational;

import lombok.Data;

import java.util.List;

@Data
public class Student  extends User {
    private List<Course> enrolledCourses;
    private List<AssignmentSubmission> submissions;
}