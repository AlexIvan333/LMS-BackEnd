package com.lms.auth_service.controllers;

import com.lms.auth_service.dtos.requests.CreateUserRequest;
import com.lms.auth_service.services.AdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth/admins")
@Validated
public class AdminController {

    private final AdminService adminService;

    @PostMapping
//    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> createAdmin(@Valid @RequestBody CreateUserRequest request) {

        var result = adminService.createAdmin(request);

        if (!result.isSuccess()) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(), result.getMessageError()
            );
            problemDetail.setTitle("Error creating the student");
            return ResponseEntity.status(result.getHttpStatus()).body(problemDetail);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.getData());
    }
}
