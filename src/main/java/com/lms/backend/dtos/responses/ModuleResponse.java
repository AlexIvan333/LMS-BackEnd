package com.lms.backend.dtos.responses;

import com.lms.backend.entities.nosql.ResourceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModuleResponse {
    private Long id;
    private String title;
    private String description;
    private List<AssignmentResponse> assignments;
    private List<ResourceResponse> resources;
    private Long courseId;
}
