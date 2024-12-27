package com.lms.backend.dtos.responses;

import com.lms.backend.entities.nosql.ResourceEntity;
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
public class AssignmentResponse {
    private Long id;
    private String title;
    private String description;
    private Date deadline;
    private Long moduleID;
    private List<ResourceResponse> resources;
}
