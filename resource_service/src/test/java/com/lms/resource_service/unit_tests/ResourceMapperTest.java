package com.lms.resource_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lms.resource_service.dtos.ResourceResponse;
import com.lms.resource_service.entites.ResourceEntity;

import java.nio.charset.StandardCharsets;

import com.lms.resource_service.mappers.ResourceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResourceMapperTest {

    @Test
    @DisplayName("Test toResponse(ResourceEntity)")
    void testToResponse() {
        // Arrange
        ResourceEntity resource = new ResourceEntity();
        resource.setFileData("Test".getBytes(StandardCharsets.UTF_8));
        resource.setFileName("foo.txt");
        resource.setFileSize(3L);
        resource.setFileType("File Type");
        resource.setId(1L);
        resource.setTitle("Dr");

        // Act
        ResourceResponse actualToResponseResult = ResourceMapper.toResponse(resource);

        // Assert
        assertEquals("Dr", actualToResponseResult.getTitle());
        assertEquals("File Type", actualToResponseResult.getFileType());
        assertEquals("foo.txt", actualToResponseResult.getFileName());
        assertEquals(1L, actualToResponseResult.getId());
        assertEquals(3L, actualToResponseResult.getFileSize());
        byte[] expectedFileData = "Test".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expectedFileData, actualToResponseResult.getFileData());
    }
}
