package com.lms.auth_service.dtos.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InstructorFilterParams {
    @Schema
    private Long instructorID;
    @Schema
    private Boolean active;
    @Schema
    private Integer page;
    @Schema
    private Integer size;
}
