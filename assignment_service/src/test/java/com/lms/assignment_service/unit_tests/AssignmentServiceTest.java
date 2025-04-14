package com.lms.assignment_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.assignment_service.dtos.responses.AssignmentResponse;
import com.lms.assignment_service.dtos.responses.ServiceResult;
import com.lms.assignment_service.entities.AssignmentEntity;
import com.lms.assignment_service.repositories.AssignmentRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {com.lms.assignment_service.services.AssignmentService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AssignmentServiceTest {
    @MockitoBean
    private AssignmentRepository assignmentRepository;

    @Autowired
    private com.lms.assignment_service.services.AssignmentService assignmentService;

    @MockitoBean
    private com.lms.assignment_service.validation.interfaces.IAssignmentValidation iAssignmentValidation;

    
    @Test
    @DisplayName("Test createAssignment(CreateAssignmentRequest); then return Data CourseID is 'null'")
    
    @MethodsUnderTest({
            "ServiceResult com.lms.assignment_service.services.AssignmentService.createAssignment(com.lms.assignment_service.dtos.requests.CreateAssignmentRequest)"})
    void testCreateAssignment_thenReturnDataCourseIDIsNull() {
        // Arrange
        AssignmentEntity assignmentEntity = new AssignmentEntity();
        assignmentEntity.setCourseId(1L);
        assignmentEntity.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignmentEntity.setDescription("The characteristics of someone or something");
        assignmentEntity.setId(1L);
        assignmentEntity.setModule_id(1L);
        assignmentEntity.setResourceIds(new ArrayList<>());
        assignmentEntity.setTitle("Dr");
        when(assignmentRepository.save(Mockito.any())).thenReturn(assignmentEntity);
        when(iAssignmentValidation.isValid(Mockito.any()))
                .thenReturn(true);

        // Act
        ServiceResult<AssignmentResponse> actualCreateAssignmentResult = assignmentService
                .createAssignment(new com.lms.assignment_service.dtos.requests.CreateAssignmentRequest());

        // Assert
        verify(iAssignmentValidation).isValid(isA(com.lms.assignment_service.dtos.requests.CreateAssignmentRequest.class));
        verify(assignmentRepository).save(isA(AssignmentEntity.class));
        AssignmentResponse data = actualCreateAssignmentResult.getData();
        assertNull(data.getCourseID());
        assertNull(data.getId());
        assertNull(data.getModuleID());
        assertNull(data.getDescription());
        assertNull(data.getTitle());
        assertNull(actualCreateAssignmentResult.getMessageError());
        assertNull(data.getDeadline());
        assertNull(data.getResources());
        assertEquals(HttpStatus.CREATED, actualCreateAssignmentResult.getHttpStatus());
        assertTrue(actualCreateAssignmentResult.isSuccess());
    }


    @Test
    @DisplayName("Test createAssignment(CreateAssignmentRequest); then return MessageError is 'The request is not valid.'")
    
    @MethodsUnderTest({
            "ServiceResult com.lms.assignment_service.services.AssignmentService.createAssignment(com.lms.assignment_service.dtos.requests.CreateAssignmentRequest)"})
    void testCreateAssignment_thenReturnMessageErrorIsTheRequestIsNotValid() {
        // Arrange
        when(iAssignmentValidation.isValid(Mockito.any()))
                .thenReturn(false);

        // Act
        ServiceResult<AssignmentResponse> actualCreateAssignmentResult = assignmentService
                .createAssignment(new com.lms.assignment_service.dtos.requests.CreateAssignmentRequest());

        // Assert
        verify(iAssignmentValidation).isValid(isA(com.lms.assignment_service.dtos.requests.CreateAssignmentRequest.class));
        assertEquals("The request is not valid.", actualCreateAssignmentResult.getMessageError());
        assertNull(actualCreateAssignmentResult.getData());
        assertEquals(HttpStatus.BAD_REQUEST, actualCreateAssignmentResult.getHttpStatus());
        assertFalse(actualCreateAssignmentResult.isSuccess());
    }


    @Test
    @DisplayName("Test getAssignments(AssignmentFilterParams); given AssignmentEntity() CourseId is one; then return size is one")
    
    @MethodsUnderTest({
            "List com.lms.assignment_service.services.AssignmentService.getAssignments(com.lms.assignment_service.dtos.filters.AssignmentFilterParams)"})
    void testGetAssignments_givenAssignmentEntityCourseIdIsOne_thenReturnSizeIsOne() {
        // Arrange
        AssignmentEntity assignmentEntity = new AssignmentEntity();
        assignmentEntity.setCourseId(1L);
        assignmentEntity.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignmentEntity.setDescription("The characteristics of someone or something");
        assignmentEntity.setId(1L);
        assignmentEntity.setModule_id(1L);
        assignmentEntity.setResourceIds(new ArrayList<>());
        assignmentEntity.setTitle("Dr");

        ArrayList<AssignmentEntity> content = new ArrayList<>();
        content.add(assignmentEntity);
        when(assignmentRepository.findAssignmentEntitiesByFilters(Mockito.<Long>any(), Mockito.<Long>any(),
                Mockito.<Long>any(), Mockito.any())).thenReturn(new PageImpl<>(content));

        com.lms.assignment_service.dtos.filters.AssignmentFilterParams filterParams = new com.lms.assignment_service.dtos.filters.AssignmentFilterParams();
        filterParams.setAssigmentId(1L);
        filterParams.setCourseId(1L);
        filterParams.setModuleId(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<AssignmentResponse> actualAssignments = assignmentService.getAssignments(filterParams);

        // Assert
        verify(assignmentRepository).findAssignmentEntitiesByFilters(eq(1L), eq(1L), eq(1L), isA(Pageable.class));
        assertEquals(1, actualAssignments.size());
        AssignmentResponse getResult = actualAssignments.getFirst();
        assertEquals("Dr", getResult.getTitle());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertEquals(1L, getResult.getCourseID().longValue());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals(1L, getResult.getModuleID().longValue());
    }


    @Test
    @DisplayName("Test getAssignments(AssignmentFilterParams); given AssignmentEntity() CourseId is two; then return size is two")
    
    @MethodsUnderTest({
            "List com.lms.assignment_service.services.AssignmentService.getAssignments(com.lms.assignment_service.dtos.filters.AssignmentFilterParams)"})
    void testGetAssignments_givenAssignmentEntityCourseIdIsTwo_thenReturnSizeIsTwo() {
        // Arrange
        AssignmentEntity assignmentEntity = new AssignmentEntity();
        assignmentEntity.setCourseId(1L);
        assignmentEntity.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignmentEntity.setDescription("The characteristics of someone or something");
        assignmentEntity.setId(1L);
        assignmentEntity.setModule_id(1L);
        assignmentEntity.setResourceIds(new ArrayList<>());
        assignmentEntity.setTitle("Dr");

        AssignmentEntity assignmentEntity2 = new AssignmentEntity();
        assignmentEntity2.setCourseId(2L);
        assignmentEntity2
                .setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignmentEntity2.setDescription("Description");
        assignmentEntity2.setId(2L);
        assignmentEntity2.setModule_id(2L);
        assignmentEntity2.setResourceIds(new ArrayList<>());
        assignmentEntity2.setTitle("Mr");

        ArrayList<AssignmentEntity> content = new ArrayList<>();
        content.add(assignmentEntity2);
        content.add(assignmentEntity);
        when(assignmentRepository.findAssignmentEntitiesByFilters(Mockito.<Long>any(), Mockito.<Long>any(),
                Mockito.<Long>any(), Mockito.any())).thenReturn(new PageImpl<>(content));

        com.lms.assignment_service.dtos.filters.AssignmentFilterParams filterParams = new com.lms.assignment_service.dtos.filters.AssignmentFilterParams();
        filterParams.setAssigmentId(1L);
        filterParams.setCourseId(1L);
        filterParams.setModuleId(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<AssignmentResponse> actualAssignments = assignmentService.getAssignments(filterParams);

        // Assert
        verify(assignmentRepository).findAssignmentEntitiesByFilters(eq(1L), eq(1L), eq(1L), isA(Pageable.class));
        assertEquals(2, actualAssignments.size());
        AssignmentResponse getResult = actualAssignments.get(0);
        assertEquals("Description", getResult.getDescription());
        AssignmentResponse getResult2 = actualAssignments.get(1);
        assertEquals("Dr", getResult2.getTitle());
        assertEquals("Mr", getResult.getTitle());
        assertEquals("The characteristics of someone or something", getResult2.getDescription());
        assertEquals(1L, getResult2.getCourseID().longValue());
        assertEquals(1L, getResult2.getId().longValue());
        assertEquals(1L, getResult2.getModuleID().longValue());
        assertEquals(2L, getResult.getCourseID().longValue());
        assertEquals(2L, getResult.getId().longValue());
        assertEquals(2L, getResult.getModuleID().longValue());
        assertTrue(getResult2.getResources().isEmpty());
    }


    @Test
    @DisplayName("Test getAssignments(AssignmentFilterParams); then calls getCourseId()")
    
    @MethodsUnderTest({
            "List com.lms.assignment_service.services.AssignmentService.getAssignments(com.lms.assignment_service.dtos.filters.AssignmentFilterParams)"})
    void testGetAssignments_thenCallsGetCourseId() {
        // Arrange
        AssignmentEntity assignmentEntity = mock(AssignmentEntity.class);
        when(assignmentEntity.getCourseId()).thenReturn(1L);
        when(assignmentEntity.getId()).thenReturn(1L);
        when(assignmentEntity.getModule_id()).thenReturn(1L);
        when(assignmentEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(assignmentEntity.getTitle()).thenReturn("Dr");
        when(assignmentEntity.getDeadline())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(assignmentEntity.getResourceIds()).thenReturn(new ArrayList<>());
        doNothing().when(assignmentEntity).setCourseId(Mockito.<Long>any());
        doNothing().when(assignmentEntity).setDeadline(Mockito.any());
        doNothing().when(assignmentEntity).setDescription(Mockito.any());
        doNothing().when(assignmentEntity).setId(Mockito.<Long>any());
        doNothing().when(assignmentEntity).setModule_id(Mockito.<Long>any());
        doNothing().when(assignmentEntity).setResourceIds(Mockito.any());
        doNothing().when(assignmentEntity).setTitle(Mockito.any());
        assignmentEntity.setCourseId(1L);
        assignmentEntity.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignmentEntity.setDescription("The characteristics of someone or something");
        assignmentEntity.setId(1L);
        assignmentEntity.setModule_id(1L);
        assignmentEntity.setResourceIds(new ArrayList<>());
        assignmentEntity.setTitle("Dr");

        ArrayList<AssignmentEntity> content = new ArrayList<>();
        content.add(assignmentEntity);
        when(assignmentRepository.findAssignmentEntitiesByFilters(Mockito.<Long>any(), Mockito.<Long>any(),
                Mockito.<Long>any(), Mockito.any())).thenReturn(new PageImpl<>(content));

        com.lms.assignment_service.dtos.filters.AssignmentFilterParams filterParams = new com.lms.assignment_service.dtos.filters.AssignmentFilterParams();
        filterParams.setAssigmentId(1L);
        filterParams.setCourseId(1L);
        filterParams.setModuleId(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<AssignmentResponse> actualAssignments = assignmentService.getAssignments(filterParams);

        // Assert
        verify(assignmentEntity).getCourseId();
        verify(assignmentEntity).getDeadline();
        verify(assignmentEntity).getDescription();
        verify(assignmentEntity).getId();
        verify(assignmentEntity).getModule_id();
        verify(assignmentEntity).getResourceIds();
        verify(assignmentEntity).getTitle();
        verify(assignmentEntity).setCourseId(eq(1L));
        verify(assignmentEntity).setDeadline(isA(Date.class));
        verify(assignmentEntity).setDescription(eq("The characteristics of someone or something"));
        verify(assignmentEntity).setId(eq(1L));
        verify(assignmentEntity).setModule_id(eq(1L));
        verify(assignmentEntity).setResourceIds(isA(List.class));
        verify(assignmentEntity).setTitle(eq("Dr"));
        verify(assignmentRepository).findAssignmentEntitiesByFilters(eq(1L), eq(1L), eq(1L), isA(Pageable.class));
        assertEquals(1, actualAssignments.size());
        AssignmentResponse getResult = actualAssignments.getFirst();
        assertEquals("Dr", getResult.getTitle());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertEquals(1L, getResult.getCourseID().longValue());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals(1L, getResult.getModuleID().longValue());
    }


    @Test
    @DisplayName("Test getAssignments(AssignmentFilterParams); then return Empty")
    
    @MethodsUnderTest({
            "List com.lms.assignment_service.services.AssignmentService.getAssignments(com.lms.assignment_service.dtos.filters.AssignmentFilterParams)"})
    void testGetAssignments_thenReturnEmpty() {
        // Arrange
        when(assignmentRepository.findAssignmentEntitiesByFilters(Mockito.<Long>any(), Mockito.<Long>any(),
                Mockito.<Long>any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        com.lms.assignment_service.dtos.filters.AssignmentFilterParams filterParams = new com.lms.assignment_service.dtos.filters.AssignmentFilterParams();
        filterParams.setAssigmentId(1L);
        filterParams.setCourseId(1L);
        filterParams.setModuleId(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<AssignmentResponse> actualAssignments = assignmentService.getAssignments(filterParams);

        // Assert
        verify(assignmentRepository).findAssignmentEntitiesByFilters(eq(1L), eq(1L), eq(1L), isA(Pageable.class));
        assertTrue(actualAssignments.isEmpty());
    }
}
