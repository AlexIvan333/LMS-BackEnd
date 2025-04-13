package com.lms.assignment_service.dtos.requests;

import jakarta.validation.constraints.NotBlank;
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
public class CreateAssignmentRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;
    private Date deadline;
    @NotBlank(message = "Course can not be null")
    private Long courseId;
    @NotBlank(message = "Module can not be null")
    private Long moduleId;
    private List<Long> resourceIds;

}
