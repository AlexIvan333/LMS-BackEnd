package com.lms.backend.domain.relational;

import lombok.Data;

import java.util.List;

@Data
public class Course {
    private Long id;
    private String title;
    private String description;
    private List<Module> modules;
    private Long instructorID;
    private Instructor instructor;
    private List<Student> enrolledStudents;
    private int maxStudents;
}
