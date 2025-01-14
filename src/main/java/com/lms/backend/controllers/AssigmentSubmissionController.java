package com.lms.backend.controllers;

import com.lms.backend.domain.enums.Grade;
import com.lms.backend.dtos.filters.AssigmentSubmissionFilterParams;
import com.lms.backend.dtos.requests.CreateAssigmentSubmissionRequest;
import com.lms.backend.dtos.responses.AssignmentSubmissionResponse;
import com.lms.backend.services.AssigmentSubmissionService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/assignmentsubmissions")
public class AssigmentSubmissionController {
    private final AssigmentSubmissionService assigmentSubmissionService;

    @PostMapping
    @RolesAllowed({"ADMIN", "STUDENT"})
    public ResponseEntity<AssignmentSubmissionResponse> createAssignmentSubmission(@RequestBody CreateAssigmentSubmissionRequest request) {

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
                                                                                       @RequestParam(required = false)Grade grade,
                                                                                       @RequestParam(required = false) Date submissionTime,
                                                                                       @RequestParam(defaultValue = "0") Integer page,
                                                                                       @RequestParam(defaultValue = "10") Integer size
                                                                                       ){

        AssigmentSubmissionFilterParams filterParams = new AssigmentSubmissionFilterParams();
        filterParams.setSubmissionId(submissionId);
        filterParams.setStudentId(studentId);
        filterParams.setAssigmentId(assigmentId);
        filterParams.setCompleted(completed);
        filterParams.setGrade(grade);
        filterParams.setSubmissionTime(submissionTime);
        filterParams.setPage(page);
        filterParams.setSize(size);

        return ResponseEntity.ok(assigmentSubmissionService.getAssignmentSubmissions(filterParams));

    }
}
