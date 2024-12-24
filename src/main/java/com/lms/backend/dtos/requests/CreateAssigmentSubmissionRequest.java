package com.lms.backend.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAssigmentSubmissionRequest {
    private Long studentId;
    private Long assigmentId;
    private List<String> resourceIds;
}
