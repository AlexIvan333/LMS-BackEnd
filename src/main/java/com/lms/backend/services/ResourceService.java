package com.lms.backend.services;


import com.lms.backend.configurations.databases.SequenceGeneratorService;
import com.lms.backend.dtos.responses.ServiceResult;
import com.lms.backend.entities.nosql.ResourceEntity;
import com.lms.backend.repositories.nosql.ResourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public ServiceResult<ResourceEntity> saveResource(MultipartFile file) throws IOException {

        String mimeType = file.getContentType();

        ResourceEntity resourceEntity = ResourceEntity.builder()
                .id(sequenceGeneratorService.generateSequence("resources_sequence"))
                .title(file.getOriginalFilename()) // File title
                .fileType(mimeType) // MIME type
                .fileName(file.getOriginalFilename()) // Original file name
                .fileSize(file.getSize()) // File size
                .fileData(file.getBytes()) // File content
                .build();

        resourceRepository.save(resourceEntity);

        return ServiceResult.<ResourceEntity>builder()
                .data(resourceEntity)
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public ResourceEntity getResourceById(Long id) {
        return resourceRepository.findResourceById(id);
    }

}
