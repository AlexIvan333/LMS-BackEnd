package com.lms.backend.dtos.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ModuleFilterParams {
    @Schema
    private Long moduleId;
    @Schema
    private Long courseID;
    @Schema
    private Integer page;
    @Schema
    private Integer size;
}
