package com.lms.backend.dtos.filters;

import com.lms.backend.domain.enums.Grade;
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
    private Date submissionTime;
    @Schema
    private Integer page;
    @Schema
    private Integer size;
}
