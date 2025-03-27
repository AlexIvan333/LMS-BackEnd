package com.lms.course_service.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddResourceToModuleRequest {
    @NotBlank(message = "Resource ID is required")
    public Long resourceId;
    @NotBlank(message = "Module ID is required")
    public Long moduleId;
}
