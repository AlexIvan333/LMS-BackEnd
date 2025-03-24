package com.lms.assignment_service.dtos.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AssigmentFilterParams {
    @Schema
    private Long assigmentId;
    @Schema
    private Long courseId;
    @Schema
    private Long moduleId;
    @Schema
    private Integer page;
    @Schema
    private Integer size;
}
