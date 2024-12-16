package com.lms.backend.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private List<ModuleResponse> modules;
    private List<StudentResponse> students;
    private Long instructorID;
    private int maxStudents;
}
