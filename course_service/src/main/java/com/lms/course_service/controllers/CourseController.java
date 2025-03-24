package com.lms.course_service.controllers;

import com.lms.course_service.dtos.filters.CourseFilterParams;
import com.lms.course_service.dtos.requests.AssignStudentToCourseRequest;
import com.lms.course_service.dtos.requests.CreateCourseRequest;
import com.lms.course_service.dtos.responses.CourseResponse;
import com.lms.course_service.services.CourseService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CreateCourseRequest request) {

        var result = courseService.createCourse(request);

        if (!result.success)
        {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(),result.getMessageError()
            );
            problemDetail.setTitle("Error creating the course");
            return ResponseEntity.of(problemDetail).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.data);
    }

    @GetMapping
    @RolesAllowed({"ADMIN", "INSTRUCTOR","STUDENT"})
    public ResponseEntity<List<CourseResponse>> getCourses( @RequestParam(required = false) Long courseId,
                                                            @RequestParam(required = false) Long instructorId,
                                                            @RequestParam(required = false) Long moduleId,
                                                            @RequestParam(required = false) Integer maxStudents,
                                                            @RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer size) {

        CourseFilterParams filterParams = new CourseFilterParams();
        filterParams.setCourseId(courseId);
        filterParams.setInstructorId(instructorId);
        filterParams.setModuleId(moduleId);
        filterParams.setMaxStudents(maxStudents);
        filterParams.setPage(page);
        filterParams.setSize(size);

        return ResponseEntity.ok(courseService.getCourses(filterParams));
    }

    @PostMapping("/assignstudent")
    @RolesAllowed({"ADMIN","STUDENT"})
    public ResponseEntity<Boolean>assignStudentToCourse (@RequestBody AssignStudentToCourseRequest request)
    {
        var result = courseService.assignStudentToCourse(request);

        if (!result.success)
        {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(),result.getMessageError()
            );
            problemDetail.setTitle("Error assigning the student to the course");
            return ResponseEntity.of(problemDetail).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result.data);
    }
}
