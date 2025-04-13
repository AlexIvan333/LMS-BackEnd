package com.lms.assignment_service.controllers;


import com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams;
import com.lms.assignment_service.dtos.requests.CreateAssignmentSubmissionRequest;
import com.lms.assignment_service.dtos.requests.GradeAssignmentSubmissionRequest;
import com.lms.assignment_service.dtos.responses.AssignmentSubmissionResponse;
import com.lms.assignment_service.entities.Grade;
import com.lms.assignment_service.services.AssignmentSubmissionService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/assignments/submissions")
public class AssignmentSubmissionController {
    private final AssignmentSubmissionService assigmentSubmissionService;

    @PostMapping
    @RolesAllowed({"ADMIN", "STUDENT"})
    public ResponseEntity<AssignmentSubmissionResponse> createAssignmentSubmission(@RequestBody CreateAssignmentSubmissionRequest request) {

        var result = assigmentSubmissionService.createAssignmentSubmission(request);

        if (!result.success)
        {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(),result.getMessageError()
            );
            problemDetail.setTitle("Error creating the assignment submission");
            return ResponseEntity.of(problemDetail).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.data);
    }

    @GetMapping
    @RolesAllowed({"ADMIN", "STUDENT", "INSTRUCTOR"})
    public ResponseEntity<List<AssignmentSubmissionResponse>> getAssignmentSubmissions(@RequestParam(required = false) Long submissionId,
                                                                                       @RequestParam(required = false) Long studentId,
                                                                                       @RequestParam(required = false) Long assigmentId,
                                                                                       @RequestParam(required = false) Boolean completed,
                                                                                       @RequestParam(required = false) Grade grade,
                                                                                       @RequestParam(defaultValue = "0") Integer page,
                                                                                       @RequestParam(defaultValue = "10") Integer size
                                                                                       ){

        AssignmentSubmissionFilterParams filterParams = new AssignmentSubmissionFilterParams();
        filterParams.setSubmissionId(submissionId);
        filterParams.setStudentId(studentId);
        filterParams.setAssigmentId(assigmentId);
        filterParams.setCompleted(completed);
        filterParams.setGrade(grade);
        filterParams.setPage(page);
        filterParams.setSize(size);

        return ResponseEntity.ok(assigmentSubmissionService.getAssignmentSubmissions(filterParams));

    }

    @PutMapping
    @RolesAllowed({"ADMIN","INSTRUCTOR"})
    public ResponseEntity<AssignmentSubmissionResponse>gradeAssignmentSubmission(@RequestBody GradeAssignmentSubmissionRequest request) {

        var result = assigmentSubmissionService.gradeAssignmentSubmission(request);

        if (!result.success)
        {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(),result.getMessageError()
            );
            problemDetail.setTitle("Error grading the assignment submission");
            return ResponseEntity.of(problemDetail).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result.data);
    }
}
