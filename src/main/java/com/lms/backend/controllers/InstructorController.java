package com.lms.backend.controllers;

import com.lms.backend.dtos.filters.InstructorFilterParams;
import com.lms.backend.dtos.requests.CreateUserRequest;
import com.lms.backend.dtos.responses.InstructorResponse;
import com.lms.backend.services.InstructorService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping
    public ResponseEntity<InstructorResponse> createInstructor(@RequestBody CreateUserRequest request) {
        var result = instructorService.registerInstructor(request);

        if (!result.success) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(),result.getMessageError()
            );
            problemDetail.setTitle("Error creating the instructor");
            return ResponseEntity.of(problemDetail).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.data);
    }

    @GetMapping
    @RolesAllowed({"ADMIN", "STUDENT"})
    ResponseEntity<List<InstructorResponse>> getInstructors( @RequestParam(required = false)Long instructorID,
                                                                            @RequestParam(required = false) Boolean active,
                                                                            @RequestParam(required = false) Long courseID,
                                                                            @RequestParam(defaultValue = "0") Integer page,
                                                                            @RequestParam(defaultValue = "10") Integer size) {
        InstructorFilterParams filterParams = new InstructorFilterParams();
        filterParams.setInstructorID(instructorID);
        filterParams.setActive(active);
        filterParams.setCourseID(courseID);
        filterParams.setPage(page);
        filterParams.setSize(size);

        return ResponseEntity.ok(instructorService.getInstructors(filterParams));

    }
}
