package com.lms.backend.mappers;


import com.lms.backend.dtos.responses.ResourceResponse;
import com.lms.backend.entities.nosql.ResourceEntity;

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
