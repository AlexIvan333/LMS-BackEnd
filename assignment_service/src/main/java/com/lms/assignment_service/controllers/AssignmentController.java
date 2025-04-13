package com.lms.assignment_service.controllers;


import com.lms.assignment_service.dtos.filters.AssignmentFilterParams;
import com.lms.assignment_service.dtos.requests.CreateAssignmentRequest;
import com.lms.assignment_service.dtos.responses.AssignmentResponse;
import com.lms.assignment_service.services.AssignmentService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assigmentService;

    @PostMapping
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    public ResponseEntity<AssignmentResponse> createAssignment(@RequestBody CreateAssignmentRequest request) {

        var result = assigmentService.createAssignment(request);

        if (!result.success)
        {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(),result.getMessageError()
            );
            problemDetail.setTitle("Error creating the assignment");
            return ResponseEntity.of(problemDetail).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.data);
    }

    @GetMapping
    @RolesAllowed({"ADMIN", "STUDENT", "INSTRUCTOR"})
    public ResponseEntity<List<AssignmentResponse>> getAssignments(@RequestParam(required = false) Long assigmentId,
                                                                   @RequestParam(required = false) Long courseId,
                                                                   @RequestParam(required = false) Long moduleId,
                                                                   @RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "10") Integer size) {

        AssignmentFilterParams filterParams = new AssignmentFilterParams();
        filterParams.setAssigmentId(assigmentId);
        filterParams.setCourseId(courseId);
        filterParams.setModuleId(moduleId);
        filterParams.setPage(page);
        filterParams.setSize(size);

        return ResponseEntity.ok(assigmentService.getAssignments(filterParams));
    }
}
