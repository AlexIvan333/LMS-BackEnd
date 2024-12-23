package com.lms.backend.dtos.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
public class StudentFilterParams {
    @Schema
    private Long studentId;
    @Schema
    private Boolean active;
    @Schema
    private Long courseID;
    @Schema
    private Long submissionID;
}
