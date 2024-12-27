package com.lms.backend.controllers;

import com.lms.backend.entities.nosql.ResourceEntity;
import com.lms.backend.services.ResourceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceService resourceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Operation(
//            summary = "Upload a resource file",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "File uploaded successfully", content = @Content(schema = @Schema(implementation = ResourceEntity.class))),
//                    @ApiResponse(responseCode = "400", description = "Invalid input"),
//                    @ApiResponse(responseCode = "500", description = "Server error")
//            }
//    )
    public ResponseEntity<ResourceEntity> uploadResource(@RequestPart("file") MultipartFile file) throws IOException {
        ResourceEntity resource = resourceService.saveResource(file).data;
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadResource(@PathVariable Long id) {
        ResourceEntity resource = resourceService.getResourceById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resource.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFileName() + "\"")
                .body(resource.getFileData());
    }
}
