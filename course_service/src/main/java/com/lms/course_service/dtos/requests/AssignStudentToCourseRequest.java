package com.lms.course_service.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignStudentToCourseRequest {
    @NotBlank(message = "Student ID is required")
    private Long student_id;

    @NotBlank(message = "Course ID  is required")
    private Long course_id;
}
