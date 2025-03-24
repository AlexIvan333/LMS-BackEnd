package com.lms.assignment_service.dtos.filters;

import com.lms.assignment_service.entities.Grade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class AssigmentSubmissionFilterParams {
    @Schema
    private Long submissionId;
    @Schema
    private Long studentId;
    @Schema
    private Long assigmentId;
    @Schema
    private Boolean completed;
    @Schema
    private Grade grade;
    @Schema
    private Integer page;
    @Schema
    private Integer size;
}
