package com.lms.backend.controllers;
import com.lms.backend.dtos.filters.StudentFilterParams;
import com.lms.backend.dtos.requests.CreateUserRequest;
import com.lms.backend.dtos.responses.StudentResponse;
import com.lms.backend.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody CreateUserRequest request) {
        var result = studentService.registerStudent(request);

        if (!result.success) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(),result.getMessageError()
            );
            problemDetail.setTitle("Error creating the student");
            return ResponseEntity.of(problemDetail).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.data);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getStudents( @RequestParam(required = false) Long studentId,
                                                              @RequestParam(required = false) Boolean active,
                                                              @RequestParam(required = false) Long courseID,
                                                              @RequestParam(required = false) Long submissionID,
                                                              @RequestParam(defaultValue = "0") Integer page,
                                                              @RequestParam(defaultValue = "10") Integer size) {

        StudentFilterParams filterParams = new StudentFilterParams();
        filterParams.setStudentId(studentId);
        filterParams.setActive(active);
        filterParams.setCourseID(courseID);
        filterParams.setSubmissionID(submissionID);
        filterParams.setPage(page);
        filterParams.setSize(size);

        return ResponseEntity.ok(studentService.getStudents(filterParams));
    }
}
