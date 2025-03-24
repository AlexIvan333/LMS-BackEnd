package com.lms.resource_service.mappers;


import com.lms.resource_service.dtos.ResourceResponse;
import com.lms.resource_service.entites.ResourceEntity;

public class ResourceMapper {

    public static ResourceResponse toResponse(ResourceEntity resource) {
        return ResourceResponse.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .fileType(resource.getFileType())
                .fileName(resource.getFileName())
                .fileSize(resource.getFileSize())
                .fileData(resource.getFileData())
                .build();
    }
}
