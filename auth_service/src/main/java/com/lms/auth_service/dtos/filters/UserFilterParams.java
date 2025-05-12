package com.lms.auth_service.dtos.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UserFilterParams {
    @Schema
    private Long studentId;
    @Schema
    private Boolean active;
    @Schema
    private String email;
    @Schema
    private Integer page;
    @Schema
    private Integer size;
}
