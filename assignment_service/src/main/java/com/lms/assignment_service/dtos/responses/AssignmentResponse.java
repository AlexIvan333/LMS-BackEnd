package com.lms.assignment_service.dtos.responses;

import com.lms.shared.dtos.ResourceResponse;
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
    private Long courseID;
    private Long moduleID;
    private List<Long> resources;
}
