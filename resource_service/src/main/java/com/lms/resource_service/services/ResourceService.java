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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public ServiceResult<ResourceEntity> saveResource(MultipartFile file) throws IOException {

        String mimeType = file.getContentType();

        ResourceEntity resourceEntity = ResourceEntity.builder()
                .id(sequenceGeneratorService.generateSequence("resources_sequence"))
                .title(file.getOriginalFilename())
                .fileType(mimeType)
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .fileData(file.getBytes())
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

    public List<ResourceResponse> getAllResources()
    {
        List<ResourceEntity> resources = resourceRepository.findAll();

        return resources.stream().map(ResourceMapper::toResponse).collect(Collectors.toList());
    }

    public ServiceResult<Void>deleteResource(Long id)
    {
        if (!resourceRepository.existsResourceEntityById(id))
        {
            return ServiceResult.<Void>builder()
                    .success(false)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .messageError("The resource was not found")
                    .build();
        }

        ResourceEntity entity = resourceRepository.findResourceById(id);
        resourceRepository.delete(entity);

        return ServiceResult.<Void>builder()
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }


}
