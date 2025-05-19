package com.lms.resource_service.controllers;


import com.lms.resource_service.dtos.ResourceResponse;
import com.lms.resource_service.entites.ResourceEntity;
import com.lms.resource_service.services.ResourceService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceService resourceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RolesAllowed({"ADMIN", "STUDENT", "INSTRUCTOR"})
    public ResponseEntity<ResourceEntity> uploadResource(@RequestPart("file") MultipartFile file) throws IOException {
        ResourceEntity resource = resourceService.saveResource(file).data;
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/download/{id}")
    @RolesAllowed({"ADMIN", "STUDENT", "INSTRUCTOR"})
    public ResponseEntity<byte[]> downloadResource(@PathVariable Long id) {
        var result = resourceService.getResourceById(id);

        if (!result.success || result.data == null) {  // ✅ Check if resource does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ResourceResponse resource = result.data;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resource.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFileName() + "\"")
                .body(resource.getFileData());
    }

    @GetMapping("/{id}")
    @RolesAllowed({"ADMIN", "STUDENT", "INSTRUCTOR"})
    public ResponseEntity<ResourceResponse>getResources(@PathVariable Long id) {

        var result = resourceService.getResourceById(id);
        if (!result.success)
        {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(),result.getMessageError()
            );
            problemDetail.setTitle("Error getting the resource");
            return ResponseEntity.of(problemDetail).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.data);
    }

    @GetMapping("/validate")
    @RolesAllowed({"ADMIN", "INSTRUCTOR", "STUDENT"})
    public ResponseEntity<List<Long>> validateResourceIds(@RequestParam List<Long> ids) {
        List<Long> existingIds = resourceService.findExistingIds(ids);
        return ResponseEntity.ok(existingIds);
    }

    @GetMapping("/all")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<List<ResourceResponse>> getAllResources() {
        List<ResourceResponse> resourceResponseList = resourceService.getAllResources();
        return ResponseEntity.ok(resourceResponseList);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({"ADMIN", "STUDENT", "INSTRUCTOR"})
    public ResponseEntity<Void>deleteResource(@PathVariable Long id) {

        var result = resourceService.deleteResource(id);
        if (!result.success)
        {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    result.getHttpStatus(),result.getMessageError()
            );
            problemDetail.setTitle("Error deleting the resource");
            return ResponseEntity.of(problemDetail).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.data);
    }

}
