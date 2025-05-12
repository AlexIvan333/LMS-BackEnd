package com.lms.auth_service.controllers;


import com.lms.auth_service.dtos.filters.InstructorFilterParams;
import com.lms.auth_service.dtos.requests.CreateUserRequest;
import com.lms.auth_service.dtos.responses.InstructorResponse;
import com.lms.auth_service.services.InstructorService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/auth/instructors")
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
                                                                            @RequestParam(required = false) String email,
                                                                            @RequestParam(defaultValue = "0") Integer page,
                                                                            @RequestParam(defaultValue = "10") Integer size) {
        InstructorFilterParams filterParams = new InstructorFilterParams();
        filterParams.setInstructorID(instructorID);
        filterParams.setActive(active);
        filterParams.setEmail(email);
        filterParams.setPage(page);
        filterParams.setSize(size);

        return ResponseEntity.ok(instructorService.getInstructors(filterParams));

    }

    @GetMapping("/isvalidedbyadmin")
    ResponseEntity<Boolean> IsValidatedByAdmin(@RequestParam Long instructorID) {
        return ResponseEntity.ok(instructorService.IsValidatedByAdmin(instructorID).data);
    }

    @GetMapping("/email")
    ResponseEntity<Long> getIdOFInstructorByEmail(@RequestParam String email) {
        return ResponseEntity.ok(instructorService.getIdOFInstructorByEmail(email));
    }
}
