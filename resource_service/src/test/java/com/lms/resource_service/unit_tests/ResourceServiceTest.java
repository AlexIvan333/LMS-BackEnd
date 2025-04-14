package com.lms.resource_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lms.resource_service.dtos.ResourceResponse;
import com.lms.resource_service.dtos.ServiceResult;
import com.lms.resource_service.entites.ResourceEntity;
import com.lms.resource_service.repositories.ResourceRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.lms.resource_service.services.ResourceService;
import com.lms.resource_service.services.SequenceGeneratorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ResourceService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ResourceServiceTest {
    @MockitoBean
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceService resourceService;

    @MockitoBean
    private SequenceGeneratorService sequenceGeneratorService;

    @Test
    @DisplayName("Test saveResource(MultipartFile); given ResourceRepository; then throw IOException")
    void testSaveResource_givenResourceRepository_thenThrowIOException() throws IOException {
        // Arrange
        when(sequenceGeneratorService.generateSequence(Mockito.any())).thenReturn(1L);
        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.getBytes()).thenThrow(new IOException("foo"));
        when(file.getContentType()).thenReturn("text/plain");
        when(file.getOriginalFilename()).thenReturn("foo.txt");
        when(file.getSize()).thenReturn(3L);

        // Act and Assert
        assertThrows(IOException.class, () -> resourceService.saveResource(file));
        verify(sequenceGeneratorService).generateSequence(eq("resources_sequence"));
        verify(file).getBytes();
        verify(file).getContentType();
        verify(file, atLeast(1)).getOriginalFilename();
        verify(file).getSize();
    }


    @Test
    @DisplayName("Test saveResource(MultipartFile); then return Data FileName is empty string")
    void testSaveResource_thenReturnDataFileNameIsEmptyString() throws IOException {
        // Arrange
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setFileData("Test".getBytes(StandardCharsets.UTF_8));
        resourceEntity.setFileName("foo.txt");
        resourceEntity.setFileSize(3L);
        resourceEntity.setFileType("File Type");
        resourceEntity.setId(1L);
        resourceEntity.setTitle("Dr");
        when(resourceRepository.save(Mockito.any())).thenReturn(resourceEntity);
        when(sequenceGeneratorService.generateSequence(Mockito.any())).thenReturn(1L);

        // Act
        ServiceResult<ResourceEntity> actualSaveResourceResult = resourceService
                .saveResource(new MockMultipartFile("Name", new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8))));

        // Assert
        verify(sequenceGeneratorService).generateSequence(eq("resources_sequence"));
        verify(resourceRepository).save(isA(ResourceEntity.class));
        ResourceEntity data = actualSaveResourceResult.getData();
        assertEquals("", data.getFileName());
        assertEquals("", data.getTitle());
        assertNull(actualSaveResourceResult.getMessageError());
        assertNull(data.getFileType());
        assertEquals(1L, data.getId());
        assertEquals(HttpStatus.CREATED, actualSaveResourceResult.getHttpStatus());
        assertTrue(actualSaveResourceResult.isSuccess());
        byte[] expectedFileData = "Test".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expectedFileData, data.getFileData());
    }

    @Test
    @DisplayName("Test getResourceById(Long); given ResourceEntity() FileData is 'Test' Bytes is 'UTF-8'")
    void testGetResourceById_givenResourceEntityFileDataIsTestBytesIsUtf8() {
        // Arrange
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setFileData("Test".getBytes(StandardCharsets.UTF_8));
        resourceEntity.setFileName("foo.txt");
        resourceEntity.setFileSize(3L);
        resourceEntity.setFileType("File Type");
        resourceEntity.setId(1L);
        resourceEntity.setTitle("Dr");
        when(resourceRepository.findResourceById(Mockito.<Long>any())).thenReturn(resourceEntity);
        when(resourceRepository.existsResourceEntityById(anyLong())).thenReturn(true);

        // Act
        ServiceResult<ResourceResponse> actualResourceById = resourceService.getResourceById(1L);

        // Assert
        verify(resourceRepository).existsResourceEntityById(eq(1L));
        verify(resourceRepository).findResourceById(eq(1L));
        ResourceResponse data = actualResourceById.getData();
        assertEquals("Dr", data.getTitle());
        assertEquals("File Type", data.getFileType());
        assertEquals("foo.txt", data.getFileName());
        assertNull(actualResourceById.getMessageError());
        assertEquals(1L, data.getId());
        assertEquals(3L, data.getFileSize());
        assertEquals(HttpStatus.CREATED, actualResourceById.getHttpStatus());
        assertTrue(actualResourceById.isSuccess());
        byte[] expectedFileData = "Test".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expectedFileData, data.getFileData());
    }


    @Test
    @DisplayName("Test getResourceById(Long); then calls getFileData()")
    void testGetResourceById_thenCallsGetFileData() {
        // Arrange
        ResourceEntity resourceEntity = mock(ResourceEntity.class);
        when(resourceEntity.getFileData()).thenReturn("Test".getBytes(StandardCharsets.UTF_8));
        when(resourceEntity.getFileName()).thenReturn("foo.txt");
        when(resourceEntity.getFileType()).thenReturn("File Type");
        when(resourceEntity.getTitle()).thenReturn("Dr");
        when(resourceEntity.getFileSize()).thenReturn(3L);
        when(resourceEntity.getId()).thenReturn(1L);
        doNothing().when(resourceEntity).setFileData(Mockito.any());
        doNothing().when(resourceEntity).setFileName(Mockito.any());
        doNothing().when(resourceEntity).setFileSize(anyLong());
        doNothing().when(resourceEntity).setFileType(Mockito.any());
        doNothing().when(resourceEntity).setId(anyLong());
        doNothing().when(resourceEntity).setTitle(Mockito.any());
        resourceEntity.setFileData("Test".getBytes(StandardCharsets.UTF_8));
        resourceEntity.setFileName("foo.txt");
        resourceEntity.setFileSize(3L);
        resourceEntity.setFileType("File Type");
        resourceEntity.setId(1L);
        resourceEntity.setTitle("Dr");
        when(resourceRepository.findResourceById(Mockito.any())).thenReturn(resourceEntity);
        when(resourceRepository.existsResourceEntityById(anyLong())).thenReturn(true);

        // Act
        ServiceResult<ResourceResponse> actualResourceById = resourceService.getResourceById(1L);

        // Assert
        verify(resourceEntity).getFileData();
        verify(resourceEntity).getFileName();
        verify(resourceEntity).getFileSize();
        verify(resourceEntity).getFileType();
        verify(resourceEntity).getId();
        verify(resourceEntity).getTitle();
        verify(resourceEntity).setFileData(isA(byte[].class));
        verify(resourceEntity).setFileName(eq("foo.txt"));
        verify(resourceEntity).setFileSize(eq(3L));
        verify(resourceEntity).setFileType(eq("File Type"));
        verify(resourceEntity).setId(eq(1L));
        verify(resourceEntity).setTitle(eq("Dr"));
        verify(resourceRepository).existsResourceEntityById(eq(1L));
        verify(resourceRepository).findResourceById(eq(1L));
        ResourceResponse data = actualResourceById.getData();
        assertEquals("Dr", data.getTitle());
        assertEquals("File Type", data.getFileType());
        assertEquals("foo.txt", data.getFileName());
        assertNull(actualResourceById.getMessageError());
        assertEquals(1L, data.getId());
        assertEquals(3L, data.getFileSize());
        assertEquals(HttpStatus.CREATED, actualResourceById.getHttpStatus());
        assertTrue(actualResourceById.isSuccess());
        byte[] expectedFileData = "Test".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expectedFileData, data.getFileData());
    }


    @Test
    @DisplayName("Test getResourceById(Long); then return MessageError is 'The resource was not found'")
    void testGetResourceById_thenReturnMessageErrorIsTheResourceWasNotFound() {
        // Arrange
        when(resourceRepository.existsResourceEntityById(anyLong())).thenReturn(false);
        ResourceEntity resourceEntity = mock(ResourceEntity.class);
        doNothing().when(resourceEntity).setFileData(Mockito.any());
        doNothing().when(resourceEntity).setFileName(Mockito.any());
        doNothing().when(resourceEntity).setFileSize(anyLong());
        doNothing().when(resourceEntity).setFileType(Mockito.any());
        doNothing().when(resourceEntity).setId(anyLong());
        doNothing().when(resourceEntity).setTitle(Mockito.any());
        resourceEntity.setFileData("Test".getBytes(StandardCharsets.UTF_8));
        resourceEntity.setFileName("foo.txt");
        resourceEntity.setFileSize(3L);
        resourceEntity.setFileType("File Type");
        resourceEntity.setId(1L);
        resourceEntity.setTitle("Dr");

        // Act
        ServiceResult<ResourceResponse> actualResourceById = resourceService.getResourceById(1L);

        // Assert
        verify(resourceEntity).setFileData(isA(byte[].class));
        verify(resourceEntity).setFileName(eq("foo.txt"));
        verify(resourceEntity).setFileSize(eq(3L));
        verify(resourceEntity).setFileType(eq("File Type"));
        verify(resourceEntity).setId(eq(1L));
        verify(resourceEntity).setTitle(eq("Dr"));
        verify(resourceRepository).existsResourceEntityById(eq(1L));
        assertEquals("The resource was not found", actualResourceById.getMessageError());
        assertNull(actualResourceById.getData());
        assertEquals(HttpStatus.NOT_FOUND, actualResourceById.getHttpStatus());
        assertFalse(actualResourceById.isSuccess());
    }


    @Test
    @DisplayName("Test findExistingIds(List); given one; when ArrayList() add one; then return Empty")
    void testFindExistingIds_givenOne_whenArrayListAddOne_thenReturnEmpty() {
        // Arrange
        when(resourceRepository.findAllById(Mockito.any())).thenReturn(new ArrayList<>());

        ArrayList<Long> ids = new ArrayList<>();
        ids.add(1L);

        // Act
        List<Long> actualFindExistingIdsResult = resourceService.findExistingIds(ids);

        // Assert
        verify(resourceRepository).findAllById(isA(Iterable.class));
        assertTrue(actualFindExistingIdsResult.isEmpty());
    }

    @Test
    @DisplayName("Test findExistingIds(List); given ResourceEntity() FileName is 'File Name'; then return size is two")
    void testFindExistingIds_givenResourceEntityFileNameIsFileName_thenReturnSizeIsTwo() {
        // Arrange
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setFileData("Test".getBytes(StandardCharsets.UTF_8));
        resourceEntity.setFileName("foo.txt");
        resourceEntity.setFileSize(3L);
        resourceEntity.setFileType("File Type");
        resourceEntity.setId(1L);
        resourceEntity.setTitle("Dr");

        ResourceEntity resourceEntity2 = new ResourceEntity();
        resourceEntity2.setFileData("Test".getBytes(StandardCharsets.UTF_8));
        resourceEntity2.setFileName("File Name");
        resourceEntity2.setFileSize(1L);
        resourceEntity2.setFileType("com.lms.resource_service.entities.ResourceEntity");
        resourceEntity2.setId(2L);
        resourceEntity2.setTitle("Mr");

        ArrayList<ResourceEntity> resourceEntityList = new ArrayList<>();
        resourceEntityList.add(resourceEntity2);
        resourceEntityList.add(resourceEntity);
        when(resourceRepository.findAllById(Mockito.any())).thenReturn(resourceEntityList);

        // Act
        List<Long> actualFindExistingIdsResult = resourceService.findExistingIds(new ArrayList<>());

        // Assert
        verify(resourceRepository).findAllById(isA(Iterable.class));
        assertEquals(2, actualFindExistingIdsResult.size());
        assertEquals(1L, actualFindExistingIdsResult.get(1).longValue());
        assertEquals(2L, actualFindExistingIdsResult.get(0).longValue());
    }

    @Test
    @DisplayName("Test findExistingIds(List); given zero; when ArrayList() add zero; then return Empty")
    void testFindExistingIds_givenZero_whenArrayListAddZero_thenReturnEmpty() {
        // Arrange
        when(resourceRepository.findAllById(Mockito.any())).thenReturn(new ArrayList<>());

        ArrayList<Long> ids = new ArrayList<>();
        ids.add(0L);
        ids.add(1L);

        // Act
        List<Long> actualFindExistingIdsResult = resourceService.findExistingIds(ids);

        // Assert
        verify(resourceRepository).findAllById(isA(Iterable.class));
        assertTrue(actualFindExistingIdsResult.isEmpty());
    }

    @Test
    @DisplayName("Test findExistingIds(List); then return size is one")
    void testFindExistingIds_thenReturnSizeIsOne() {
        // Arrange
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setFileData("Test".getBytes(StandardCharsets.UTF_8));
        resourceEntity.setFileName("foo.txt");
        resourceEntity.setFileSize(3L);
        resourceEntity.setFileType("File Type");
        resourceEntity.setId(1L);
        resourceEntity.setTitle("Dr");

        ArrayList<ResourceEntity> resourceEntityList = new ArrayList<>();
        resourceEntityList.add(resourceEntity);
        when(resourceRepository.findAllById(Mockito.any())).thenReturn(resourceEntityList);

        // Act
        List<Long> actualFindExistingIdsResult = resourceService.findExistingIds(new ArrayList<>());

        // Assert
        verify(resourceRepository).findAllById(isA(Iterable.class));
        assertEquals(1, actualFindExistingIdsResult.size());
        assertEquals(1L, actualFindExistingIdsResult.getFirst().longValue());
    }

    @Test
    @DisplayName("Test findExistingIds(List); when ArrayList(); then return Empty")
    void testFindExistingIds_whenArrayList_thenReturnEmpty() {
        // Arrange
        when(resourceRepository.findAllById(Mockito.any())).thenReturn(new ArrayList<>());

        // Act
        List<Long> actualFindExistingIdsResult = resourceService.findExistingIds(new ArrayList<>());

        // Assert
        verify(resourceRepository).findAllById(isA(Iterable.class));
        assertTrue(actualFindExistingIdsResult.isEmpty());
    }
}
