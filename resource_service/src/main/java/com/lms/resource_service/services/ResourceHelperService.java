package com.lms.resource_service.services;


import com.lms.resource_service.entites.ResourceEntity;
import com.lms.resource_service.repositories.ResourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPOutputStream;

@Service
@AllArgsConstructor
public class ResourceHelperService {
    private final ResourceRepository resourceRepository;

    public List<ResourceEntity> populateResources(List<Long> resourceIds) {
        if (resourceIds != null && !resourceIds.isEmpty()) {
            return resourceRepository.findAllById(resourceIds);
        }
        return Collections.emptyList();
    }

    public byte[] compressData(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(data);
        }//todo:to delete
        return byteArrayOutputStream.toByteArray();
    }
}
