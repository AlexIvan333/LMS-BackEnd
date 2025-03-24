package com.lms.course_service.dtos.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CourseFilterParams {
    @Schema
    private Long courseId;
    @Schema
    private Long instructorId;
    @Schema
    private Long moduleId;
    @Schema
    private Integer maxStudents;
    @Schema
    private Integer page;
    @Schema
    private Integer size;
}
