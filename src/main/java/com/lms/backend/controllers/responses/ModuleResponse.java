package com.lms.backend.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.ast.tree.update.Assignment;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModuleResponse {
    private Long id;
    private String title;
    private String description;
    private List<AssignmentResponse> assignments;
    private List<Long> resourceIds;
    private Long courseId;
}
