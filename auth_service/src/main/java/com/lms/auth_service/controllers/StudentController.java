package com.lms.auth_service.controllers;

import com.lms.auth_service.dtos.filters.UserFilterParams;
import com.lms.auth_service.dtos.requests.CreateUserRequest;
import com.lms.auth_service.dtos.responses.StudentResponse;
import com.lms.auth_service.services.StudentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth/students")
@Validated
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody CreateUserRequest request) {

        var result = studentService.registerStudent(request);

        if (!result.isSuccess()) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(), result.getMessageError()
            );
            problemDetail.setTitle("Error creating the student");
            return ResponseEntity.status(result.getHttpStatus()).body(problemDetail);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.getData());
    }


    @GetMapping
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    public ResponseEntity<List<StudentResponse>> getStudents(@RequestParam(required = false) Long studentId,
                                                             @RequestParam(required = false) Boolean active,
                                                             @RequestParam(required = false) String email,
                                                             @RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "10") Integer size) {

        UserFilterParams filterParams = new UserFilterParams();
        filterParams.setStudentId(studentId);
        filterParams.setActive(active);
        filterParams.setEmail(email);
        filterParams.setPage(page);
        filterParams.setSize(size);

        return ResponseEntity.ok(studentService.getStudents(filterParams));
    }
}
