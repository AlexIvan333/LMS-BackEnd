package com.lms.backend.domain.relational;

import lombok.Data;

import java.util.List;

@Data
public class Instructor extends User {
    private List<Course> createdCourses;
}
