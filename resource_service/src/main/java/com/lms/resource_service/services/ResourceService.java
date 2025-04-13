package com.lms.resource_service.services;



import com.lms.resource_service.dtos.ResourceResponse;
import com.lms.resource_service.dtos.ServiceResult;
import com.lms.resource_service.entites.ResourceEntity;
import com.lms.resource_service.mappers.ResourceMapper;
import com.lms.resource_service.repositories.ResourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    public ServiceResult<ResourceResponse> getResourceById(Long id)
    {
        if (!resourceRepository.existsResourceEntityById(id))
        {
            return ServiceResult.<ResourceResponse>builder()
                    .success(false)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .messageError("The resource was not found")
                    .build();
        }

        ResourceEntity entity = resourceRepository.findResourceById(id);


        return ServiceResult.<ResourceResponse>builder()
                .data(ResourceMapper.toResponse(entity))
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public List<Long> findExistingIds(List<Long> ids) {
        return resourceRepository.findAllById(ids).stream()
                .map(ResourceEntity::getId)
                .toList();
    }


}
