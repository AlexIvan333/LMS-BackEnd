package com.lms.backend.controllers;

import com.lms.backend.dtos.filters.ModuleFilterParams;
import com.lms.backend.dtos.requests.CreateModuleRequest;
import com.lms.backend.dtos.responses.ModuleResponse;
import com.lms.backend.services.ModuleService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    public ResponseEntity<ModuleResponse> createModule(@RequestBody CreateModuleRequest request) {

        var result = moduleService.createModule(request);

        if (!result.success)
        {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(),result.getMessageError()
            );
            problemDetail.setTitle("Error creating the module");
            return ResponseEntity.of(problemDetail).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.data);
    }

    @GetMapping
    @RolesAllowed({"ADMIN", "STUDENT", "INSTRUCTOR"})
    public ResponseEntity<List<ModuleResponse>> getModules(@RequestParam(required = false) Long moduleId,
                                                           @RequestParam(required = false) Long courseId,
                                                           @RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "10") Integer size) {

        ModuleFilterParams filterParams = new ModuleFilterParams();
        filterParams.setModuleId(moduleId);
        filterParams.setPage(page);
        filterParams.setSize(size);
        filterParams.setCourseID(courseId);

        return ResponseEntity.ok(moduleService.getModules(filterParams));

    }
}
