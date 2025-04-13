package com.lms.assignment_service.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAssignmentSubmissionRequest {
    @NotBlank(message = "Student can not be null")
    private Long studentId;
    @NotBlank(message = "Assignment can not be null")
    private Long assigmentId;
    private List<Long> resourceIds;
}
