package com.lms.resource_service.controllers;


import com.lms.resource_service.dtos.ResourceResponse;
import com.lms.resource_service.entites.ResourceEntity;
import com.lms.resource_service.services.ResourceService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        ResourceResponse resource = resourceService.getResourceById(id).data;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resource.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFileName() + "\"")
                .body(resource.getFileData());
    }

    @GetMapping("/{id}")
    @RolesAllowed({"ADMIN", "STUDENT", "INSTRUCTOR"})
    public ResponseEntity<ResourceResponse>getResources(@PathVariable Long id) {

        return ResponseEntity.ok(resourceService.getResourceById(id).data);
    }


}
