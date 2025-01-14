package com.lms.backend.controllers;

import com.lms.backend.dtos.filters.AssigmentFilterParams;
import com.lms.backend.dtos.requests.CreateAssigmentRequest;
import com.lms.backend.dtos.responses.AssignmentResponse;
import com.lms.backend.services.AssigmentService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//get filtes: completed, student id, page, size, course, module iD
//grade and comment assigment with complete
//create assigment
//create assigment submission date= date.now when press (start assigment)
@RestController
@AllArgsConstructor
@RequestMapping("/assignments")
public class AssigmentController {

    private final AssigmentService assigmentService;

    @PostMapping
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    public ResponseEntity<AssignmentResponse> createAssignment(@RequestBody CreateAssigmentRequest request) {

        var result = assigmentService.createAssigment(request);

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

        AssigmentFilterParams filterParams = new AssigmentFilterParams();
        filterParams.setAssigmentId(assigmentId);
        filterParams.setCourseId(courseId);
        filterParams.setModuleId(moduleId);
        filterParams.setPage(page);
        filterParams.setSize(size);

        return ResponseEntity.ok(assigmentService.getAssigments(filterParams));
    }
}
