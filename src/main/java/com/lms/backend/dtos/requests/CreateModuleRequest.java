package com.lms.backend.dtos.requests;

import com.lms.backend.entities.nosql.ResourceEntity;
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
public class CreateModuleRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Course can not be null")
    private Long courseId;
    private List<Long> resourceIds;
}
