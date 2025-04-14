package com.lms.assignment_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.assignment_service.dtos.requests.GradeAssignmentSubmissionRequest;
import com.lms.assignment_service.dtos.responses.AssignmentResponse;
import com.lms.assignment_service.dtos.responses.AssignmentSubmissionResponse;
import com.lms.assignment_service.dtos.responses.ServiceResult;
import com.lms.assignment_service.entities.AssignmentEntity;
import com.lms.assignment_service.entities.AssignmentSubmissionEntity;
import com.lms.assignment_service.entities.Grade;
import com.lms.assignment_service.repositories.AssignmentRepository;
import com.lms.assignment_service.repositories.AssignmentSubmissionRepository;

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

@ContextConfiguration(classes = {com.lms.assignment_service.services.AssignmentSubmissionService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AssignmentSubmissionServiceTest {
    @MockitoBean
    private AssignmentRepository assignmentRepository;

    @MockitoBean
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    @Autowired
    private com.lms.assignment_service.services.AssignmentSubmissionService assignmentSubmissionService;

    @MockitoBean
    private com.lms.assignment_service.validation.interfaces.IAssignmentSubmissionValidation iAssignmentSubmissionValidation;

    
    @Test
    @DisplayName("Test createAssignmentSubmission(CreateAssignmentSubmissionRequest)")
    
    @MethodsUnderTest({
            "ServiceResult com.lms.assignment_service.services.AssignmentSubmissionService.createAssignmentSubmission(com.lms.assignment_service.dtos.requests.CreateAssignmentSubmissionRequest)"})
    void testCreateAssignmentSubmission() {
        // Arrange
        AssignmentEntity assignment = new AssignmentEntity();
        assignment.setCourseId(1L);
        assignment.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignment.setDescription("The characteristics of someone or something");
        assignment.setId(1L);
        assignment.setModule_id(1L);
        assignment.setResourceIds(new ArrayList<>());
        assignment.setTitle("Dr");

        AssignmentSubmissionEntity assignmentSubmissionEntity = new AssignmentSubmissionEntity();
        assignmentSubmissionEntity.setAssignment(assignment);
        assignmentSubmissionEntity.setComment("Comment");
        assignmentSubmissionEntity.setCompleted(true);
        assignmentSubmissionEntity.setGrade(Grade.UNDEFINED);
        assignmentSubmissionEntity.setId(1L);
        assignmentSubmissionEntity.setResourceIds(new ArrayList<>());
        assignmentSubmissionEntity.setStudent_id(1L);
        assignmentSubmissionEntity
                .setSubmission_time(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(assignmentSubmissionRepository.save(Mockito.any()))
                .thenReturn(assignmentSubmissionEntity);

        AssignmentEntity assignmentEntity = new AssignmentEntity();
        assignmentEntity.setCourseId(1L);
        assignmentEntity.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignmentEntity.setDescription("The characteristics of someone or something");
        assignmentEntity.setId(1L);
        assignmentEntity.setModule_id(1L);
        assignmentEntity.setResourceIds(new ArrayList<>());
        assignmentEntity.setTitle("Dr");
        when(assignmentRepository.findAssignmentEntitiesById(Mockito.<Long>any())).thenReturn(assignmentEntity);
        when(iAssignmentSubmissionValidation
                .isValid(Mockito.any()))
                .thenReturn(true);

        // Act
        ServiceResult<AssignmentSubmissionResponse> actualCreateAssignmentSubmissionResult = assignmentSubmissionService
                .createAssignmentSubmission(new com.lms.assignment_service.dtos.requests.CreateAssignmentSubmissionRequest());

        // Assert
        verify(assignmentRepository).findAssignmentEntitiesById(isNull());
        verify(iAssignmentSubmissionValidation)
                .isValid(isA(com.lms.assignment_service.dtos.requests.CreateAssignmentSubmissionRequest.class));
        verify(assignmentSubmissionRepository).save(isA(AssignmentSubmissionEntity.class));
        AssignmentSubmissionResponse data = actualCreateAssignmentSubmissionResult.getData();
        assertEquals("", data.getComment());
        assertNull(data.getId());
        assertNull(data.getStudentID());
        assertNull(actualCreateAssignmentSubmissionResult.getMessageError());
        assertNull(data.getResources());
        assertEquals(Grade.UNDEFINED, data.getGrade());
        assertEquals(HttpStatus.CREATED, actualCreateAssignmentSubmissionResult.getHttpStatus());
        assertFalse(data.getCompleted());
        assertTrue(actualCreateAssignmentSubmissionResult.isSuccess());
    }

    
    @Test
    @DisplayName("Test getAssignmentSubmissions(AssignmentSubmissionFilterParams); then calls getAssignment()")
    
    @MethodsUnderTest({
            "List com.lms.assignment_service.services.AssignmentSubmissionService.getAssignmentSubmissions(com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams)"})
    void testGetAssignmentSubmissions_thenCallsGetAssignment() {
        // Arrange
        AssignmentEntity assignment = new AssignmentEntity();
        assignment.setCourseId(1L);
        assignment.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignment.setDescription("The characteristics of someone or something");
        assignment.setId(1L);
        assignment.setModule_id(1L);
        assignment.setResourceIds(new ArrayList<>());
        assignment.setTitle("Dr");

        AssignmentEntity assignmentEntity = new AssignmentEntity();
        assignmentEntity.setCourseId(1L);
        assignmentEntity.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignmentEntity.setDescription("The characteristics of someone or something");
        assignmentEntity.setId(1L);
        assignmentEntity.setModule_id(1L);
        assignmentEntity.setResourceIds(new ArrayList<>());
        assignmentEntity.setTitle("Dr");
        AssignmentSubmissionEntity assignmentSubmissionEntity = mock(AssignmentSubmissionEntity.class);
        when(assignmentSubmissionEntity.getAssignment()).thenReturn(assignmentEntity);
        when(assignmentSubmissionEntity.getGrade()).thenReturn(Grade.UNDEFINED);
        when(assignmentSubmissionEntity.getCompleted()).thenReturn(true);
        when(assignmentSubmissionEntity.getId()).thenReturn(1L);
        when(assignmentSubmissionEntity.getStudent_id()).thenReturn(1L);
        when(assignmentSubmissionEntity.getComment()).thenReturn("Comment");
        when(assignmentSubmissionEntity.getSubmission_time())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(assignmentSubmissionEntity.getResourceIds()).thenReturn(new ArrayList<>());
        doNothing().when(assignmentSubmissionEntity).setAssignment(Mockito.any());
        doNothing().when(assignmentSubmissionEntity).setComment(Mockito.any());
        doNothing().when(assignmentSubmissionEntity).setCompleted(Mockito.<Boolean>any());
        doNothing().when(assignmentSubmissionEntity).setGrade(Mockito.any());
        doNothing().when(assignmentSubmissionEntity).setId(Mockito.<Long>any());
        doNothing().when(assignmentSubmissionEntity).setResourceIds(Mockito.any());
        doNothing().when(assignmentSubmissionEntity).setStudent_id(Mockito.<Long>any());
        doNothing().when(assignmentSubmissionEntity).setSubmission_time(Mockito.any());
        assignmentSubmissionEntity.setAssignment(assignment);
        assignmentSubmissionEntity.setComment("Comment");
        assignmentSubmissionEntity.setCompleted(true);
        assignmentSubmissionEntity.setGrade(Grade.UNDEFINED);
        assignmentSubmissionEntity.setId(1L);
        assignmentSubmissionEntity.setResourceIds(new ArrayList<>());
        assignmentSubmissionEntity.setStudent_id(1L);
        assignmentSubmissionEntity
                .setSubmission_time(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        ArrayList<AssignmentSubmissionEntity> content = new ArrayList<>();
        content.add(assignmentSubmissionEntity);
        when(assignmentSubmissionRepository.getAssignmentSubmissionsEntitiesByFilters(Mockito.<Long>any(),
                Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.any(),
                Mockito.any())).thenReturn(new PageImpl<>(content));

        com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams filterParams = new com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams();
        filterParams.setAssigmentId(1L);
        filterParams.setCompleted(true);
        filterParams.setGrade(Grade.UNDEFINED);
        filterParams.setPage(1);
        filterParams.setSize(3);
        filterParams.setStudentId(1L);
        filterParams.setSubmissionId(1L);

        // Act
        List<AssignmentSubmissionResponse> actualAssignmentSubmissions = assignmentSubmissionService
                .getAssignmentSubmissions(filterParams);

        // Assert
        verify(assignmentSubmissionEntity).getAssignment();
        verify(assignmentSubmissionEntity).getComment();
        verify(assignmentSubmissionEntity).getCompleted();
        verify(assignmentSubmissionEntity).getGrade();
        verify(assignmentSubmissionEntity).getId();
        verify(assignmentSubmissionEntity).getResourceIds();
        verify(assignmentSubmissionEntity).getStudent_id();
        verify(assignmentSubmissionEntity).getSubmission_time();
        verify(assignmentSubmissionEntity).setAssignment(isA(AssignmentEntity.class));
        verify(assignmentSubmissionEntity).setComment(eq("Comment"));
        verify(assignmentSubmissionEntity).setCompleted(eq(true));
        verify(assignmentSubmissionEntity).setGrade(eq(Grade.UNDEFINED));
        verify(assignmentSubmissionEntity).setId(eq(1L));
        verify(assignmentSubmissionEntity).setResourceIds(isA(List.class));
        verify(assignmentSubmissionEntity).setStudent_id(eq(1L));
        verify(assignmentSubmissionEntity).setSubmission_time(isA(Date.class));
        verify(assignmentSubmissionRepository).getAssignmentSubmissionsEntitiesByFilters(eq(1L), eq(1L), eq(1L), eq(true),
                eq(Grade.UNDEFINED), isA(Pageable.class));
        assertEquals(1, actualAssignmentSubmissions.size());
        AssignmentSubmissionResponse getResult = actualAssignmentSubmissions.getFirst();
        assertEquals("Comment", getResult.getComment());
        AssignmentResponse assignmentResponse = getResult.getAssignmentResponse();
        assertEquals("Dr", assignmentResponse.getTitle());
        assertEquals("The characteristics of someone or something", assignmentResponse.getDescription());
        assertEquals(1L, assignmentResponse.getCourseID().longValue());
        assertEquals(1L, assignmentResponse.getId().longValue());
        assertEquals(1L, assignmentResponse.getModuleID().longValue());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals(1L, getResult.getStudentID().longValue());
        assertEquals(Grade.UNDEFINED, getResult.getGrade());
        assertTrue(getResult.getCompleted());
    }

    
    @Test
    @DisplayName("Test getAssignmentSubmissions(AssignmentSubmissionFilterParams); then return Empty")
    
    @MethodsUnderTest({
            "List com.lms.assignment_service.services.AssignmentSubmissionService.getAssignmentSubmissions(com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams)"})
    void testGetAssignmentSubmissions_thenReturnEmpty() {
        // Arrange
        when(assignmentSubmissionRepository.getAssignmentSubmissionsEntitiesByFilters(Mockito.<Long>any(),
                Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.any(),
                Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams filterParams = new com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams();
        filterParams.setAssigmentId(1L);
        filterParams.setCompleted(true);
        filterParams.setGrade(Grade.UNDEFINED);
        filterParams.setPage(1);
        filterParams.setSize(3);
        filterParams.setStudentId(1L);
        filterParams.setSubmissionId(1L);

        // Act
        List<AssignmentSubmissionResponse> actualAssignmentSubmissions = assignmentSubmissionService
                .getAssignmentSubmissions(filterParams);

        // Assert
        verify(assignmentSubmissionRepository).getAssignmentSubmissionsEntitiesByFilters(eq(1L), eq(1L), eq(1L), eq(true),
                eq(Grade.UNDEFINED), isA(Pageable.class));
        assertTrue(actualAssignmentSubmissions.isEmpty());
    }

   
    @Test
    @DisplayName("Test getAssignmentSubmissions(AssignmentSubmissionFilterParams); then return size is one")
    
    @MethodsUnderTest({
            "List com.lms.assignment_service.services.AssignmentSubmissionService.getAssignmentSubmissions(com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams)"})
    void testGetAssignmentSubmissions_thenReturnSizeIsOne() {
        // Arrange
        AssignmentEntity assignment = new AssignmentEntity();
        assignment.setCourseId(1L);
        assignment.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignment.setDescription("The characteristics of someone or something");
        assignment.setId(1L);
        assignment.setModule_id(1L);
        assignment.setResourceIds(new ArrayList<>());
        assignment.setTitle("Dr");

        AssignmentSubmissionEntity assignmentSubmissionEntity = new AssignmentSubmissionEntity();
        assignmentSubmissionEntity.setAssignment(assignment);
        assignmentSubmissionEntity.setComment("Comment");
        assignmentSubmissionEntity.setCompleted(true);
        assignmentSubmissionEntity.setGrade(Grade.UNDEFINED);
        assignmentSubmissionEntity.setId(1L);
        assignmentSubmissionEntity.setResourceIds(new ArrayList<>());
        assignmentSubmissionEntity.setStudent_id(1L);
        assignmentSubmissionEntity
                .setSubmission_time(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        ArrayList<AssignmentSubmissionEntity> content = new ArrayList<>();
        content.add(assignmentSubmissionEntity);
        when(assignmentSubmissionRepository.getAssignmentSubmissionsEntitiesByFilters(Mockito.<Long>any(),
                Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.any(),
                Mockito.any())).thenReturn(new PageImpl<>(content));

        com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams filterParams = new com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams();
        filterParams.setAssigmentId(1L);
        filterParams.setCompleted(true);
        filterParams.setGrade(Grade.UNDEFINED);
        filterParams.setPage(1);
        filterParams.setSize(3);
        filterParams.setStudentId(1L);
        filterParams.setSubmissionId(1L);

        // Act
        List<AssignmentSubmissionResponse> actualAssignmentSubmissions = assignmentSubmissionService
                .getAssignmentSubmissions(filterParams);

        // Assert
        verify(assignmentSubmissionRepository).getAssignmentSubmissionsEntitiesByFilters(eq(1L), eq(1L), eq(1L), eq(true),
                eq(Grade.UNDEFINED), isA(Pageable.class));
        assertEquals(1, actualAssignmentSubmissions.size());
        AssignmentSubmissionResponse getResult = actualAssignmentSubmissions.getFirst();
        assertEquals("Comment", getResult.getComment());
        AssignmentResponse assignmentResponse = getResult.getAssignmentResponse();
        assertEquals("Dr", assignmentResponse.getTitle());
        assertEquals("The characteristics of someone or something", assignmentResponse.getDescription());
        assertEquals(1L, assignmentResponse.getCourseID().longValue());
        assertEquals(1L, assignmentResponse.getId().longValue());
        assertEquals(1L, assignmentResponse.getModuleID().longValue());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals(1L, getResult.getStudentID().longValue());
        assertEquals(Grade.UNDEFINED, getResult.getGrade());
        assertTrue(getResult.getCompleted());
    }

    
    @Test
    @DisplayName("Test getAssignmentSubmissions(AssignmentSubmissionFilterParams); then return size is two")
    
    @MethodsUnderTest({
            "List com.lms.assignment_service.services.AssignmentSubmissionService.getAssignmentSubmissions(com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams)"})
    void testGetAssignmentSubmissions_thenReturnSizeIsTwo() {
        // Arrange
        AssignmentEntity assignment = new AssignmentEntity();
        assignment.setCourseId(1L);
        assignment.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignment.setDescription("The characteristics of someone or something");
        assignment.setId(1L);
        assignment.setModule_id(1L);
        assignment.setResourceIds(new ArrayList<>());
        assignment.setTitle("Dr");

        AssignmentSubmissionEntity assignmentSubmissionEntity = new AssignmentSubmissionEntity();
        assignmentSubmissionEntity.setAssignment(assignment);
        assignmentSubmissionEntity.setComment("Comment");
        assignmentSubmissionEntity.setCompleted(true);
        assignmentSubmissionEntity.setGrade(Grade.UNDEFINED);
        assignmentSubmissionEntity.setId(1L);
        assignmentSubmissionEntity.setResourceIds(new ArrayList<>());
        assignmentSubmissionEntity.setStudent_id(1L);
        assignmentSubmissionEntity
                .setSubmission_time(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        AssignmentEntity assignment2 = new AssignmentEntity();
        assignment2.setCourseId(2L);
        assignment2.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignment2.setDescription("Description");
        assignment2.setId(2L);
        assignment2.setModule_id(2L);
        assignment2.setResourceIds(new ArrayList<>());
        assignment2.setTitle("Mr");

        AssignmentSubmissionEntity assignmentSubmissionEntity2 = new AssignmentSubmissionEntity();
        assignmentSubmissionEntity2.setAssignment(assignment2);
        assignmentSubmissionEntity2.setComment("com.lms.assignment_service.entities.AssignmentSubmissionEntity");
        assignmentSubmissionEntity2.setCompleted(false);
        assignmentSubmissionEntity2.setGrade(Grade.ORIENTING);
        assignmentSubmissionEntity2.setId(2L);
        assignmentSubmissionEntity2.setResourceIds(new ArrayList<>());
        assignmentSubmissionEntity2.setStudent_id(2L);
        assignmentSubmissionEntity2
                .setSubmission_time(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        ArrayList<AssignmentSubmissionEntity> content = new ArrayList<>();
        content.add(assignmentSubmissionEntity2);
        content.add(assignmentSubmissionEntity);
        when(assignmentSubmissionRepository.getAssignmentSubmissionsEntitiesByFilters(Mockito.<Long>any(),
                Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<Boolean>any(), Mockito.any(),
                Mockito.any())).thenReturn(new PageImpl<>(content));

        com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams filterParams = new com.lms.assignment_service.dtos.filters.AssignmentSubmissionFilterParams();
        filterParams.setAssigmentId(1L);
        filterParams.setCompleted(true);
        filterParams.setGrade(Grade.UNDEFINED);
        filterParams.setPage(1);
        filterParams.setSize(3);
        filterParams.setStudentId(1L);
        filterParams.setSubmissionId(1L);

        // Act
        List<AssignmentSubmissionResponse> actualAssignmentSubmissions = assignmentSubmissionService
                .getAssignmentSubmissions(filterParams);

        // Assert
        verify(assignmentSubmissionRepository).getAssignmentSubmissionsEntitiesByFilters(eq(1L), eq(1L), eq(1L), eq(true),
                eq(Grade.UNDEFINED), isA(Pageable.class));
        assertEquals(2, actualAssignmentSubmissions.size());
        AssignmentSubmissionResponse getResult = actualAssignmentSubmissions.get(1);
        assertEquals("Comment", getResult.getComment());
        AssignmentSubmissionResponse getResult2 = actualAssignmentSubmissions.get(0);
        assertEquals("com.lms.assignment_service.entities.AssignmentSubmissionEntity", getResult2.getComment());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals(1L, getResult.getStudentID().longValue());
        assertEquals(2L, getResult2.getId().longValue());
        assertEquals(2L, getResult2.getStudentID().longValue());
        assertEquals(Grade.ORIENTING, getResult2.getGrade());
        assertEquals(Grade.UNDEFINED, getResult.getGrade());
        assertFalse(getResult2.getCompleted());
        assertTrue(getResult.getCompleted());
        assertTrue(getResult.getResources().isEmpty());
    }

    
    @Test
    @DisplayName("Test gradeAssignmentSubmission(GradeAssignmentSubmissionRequest); then return Data Comment is 'Comment'")
    
    @MethodsUnderTest({
            "ServiceResult com.lms.assignment_service.services.AssignmentSubmissionService.gradeAssignmentSubmission(GradeAssignmentSubmissionRequest)"})
    void testGradeAssignmentSubmission_thenReturnDataCommentIsComment() {
        // Arrange
        AssignmentEntity assignment = new AssignmentEntity();
        assignment.setCourseId(1L);
        assignment.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignment.setDescription("The characteristics of someone or something");
        assignment.setId(1L);
        assignment.setModule_id(1L);
        assignment.setResourceIds(new ArrayList<>());
        assignment.setTitle("Dr");

        AssignmentEntity assignmentEntity = new AssignmentEntity();
        assignmentEntity.setCourseId(1L);
        assignmentEntity.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignmentEntity.setDescription("The characteristics of someone or something");
        assignmentEntity.setId(1L);
        assignmentEntity.setModule_id(1L);
        assignmentEntity.setResourceIds(new ArrayList<>());
        assignmentEntity.setTitle("Dr");
        AssignmentSubmissionEntity assignmentSubmissionEntity = mock(AssignmentSubmissionEntity.class);
        when(assignmentSubmissionEntity.getAssignment()).thenReturn(assignmentEntity);
        when(assignmentSubmissionEntity.getGrade()).thenReturn(Grade.UNDEFINED);
        when(assignmentSubmissionEntity.getCompleted()).thenReturn(true);
        when(assignmentSubmissionEntity.getId()).thenReturn(1L);
        when(assignmentSubmissionEntity.getStudent_id()).thenReturn(1L);
        when(assignmentSubmissionEntity.getComment()).thenReturn("Comment");
        when(assignmentSubmissionEntity.getSubmission_time())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(assignmentSubmissionEntity.getResourceIds()).thenReturn(new ArrayList<>());
        doNothing().when(assignmentSubmissionEntity).setAssignment(Mockito.any());
        doNothing().when(assignmentSubmissionEntity).setComment(Mockito.any());
        doNothing().when(assignmentSubmissionEntity).setCompleted(Mockito.<Boolean>any());
        doNothing().when(assignmentSubmissionEntity).setGrade(Mockito.any());
        doNothing().when(assignmentSubmissionEntity).setId(Mockito.<Long>any());
        doNothing().when(assignmentSubmissionEntity).setResourceIds(Mockito.any());
        doNothing().when(assignmentSubmissionEntity).setStudent_id(Mockito.<Long>any());
        doNothing().when(assignmentSubmissionEntity).setSubmission_time(Mockito.any());
        assignmentSubmissionEntity.setAssignment(assignment);
        assignmentSubmissionEntity.setComment("Comment");
        assignmentSubmissionEntity.setCompleted(true);
        assignmentSubmissionEntity.setGrade(Grade.UNDEFINED);
        assignmentSubmissionEntity.setId(1L);
        assignmentSubmissionEntity.setResourceIds(new ArrayList<>());
        assignmentSubmissionEntity.setStudent_id(1L);
        assignmentSubmissionEntity
                .setSubmission_time(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        AssignmentEntity assignment2 = new AssignmentEntity();
        assignment2.setCourseId(1L);
        assignment2.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignment2.setDescription("The characteristics of someone or something");
        assignment2.setId(1L);
        assignment2.setModule_id(1L);
        assignment2.setResourceIds(new ArrayList<>());
        assignment2.setTitle("Dr");

        AssignmentSubmissionEntity assignmentSubmissionEntity2 = new AssignmentSubmissionEntity();
        assignmentSubmissionEntity2.setAssignment(assignment2);
        assignmentSubmissionEntity2.setComment("Comment");
        assignmentSubmissionEntity2.setCompleted(true);
        assignmentSubmissionEntity2.setGrade(Grade.UNDEFINED);
        assignmentSubmissionEntity2.setId(1L);
        assignmentSubmissionEntity2.setResourceIds(new ArrayList<>());
        assignmentSubmissionEntity2.setStudent_id(1L);
        assignmentSubmissionEntity2
                .setSubmission_time(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(assignmentSubmissionRepository.save(Mockito.any()))
                .thenReturn(assignmentSubmissionEntity2);
        when(assignmentSubmissionRepository.findAssignmentSubmissionEntityById(Mockito.<Long>any()))
                .thenReturn(assignmentSubmissionEntity);
        when(iAssignmentSubmissionValidation.Exists(Mockito.<Long>any())).thenReturn(true);

        // Act
        ServiceResult<AssignmentSubmissionResponse> actualGradeAssignmentSubmissionResult = assignmentSubmissionService
                .gradeAssignmentSubmission(new GradeAssignmentSubmissionRequest());

        // Assert
        verify(assignmentSubmissionEntity).getAssignment();
        verify(assignmentSubmissionEntity).getComment();
        verify(assignmentSubmissionEntity).getCompleted();
        verify(assignmentSubmissionEntity).getGrade();
        verify(assignmentSubmissionEntity).getId();
        verify(assignmentSubmissionEntity).getResourceIds();
        verify(assignmentSubmissionEntity).getStudent_id();
        verify(assignmentSubmissionEntity).getSubmission_time();
        verify(assignmentSubmissionEntity).setAssignment(isA(AssignmentEntity.class));
        verify(assignmentSubmissionEntity, atLeast(1)).setComment(Mockito.any());
        verify(assignmentSubmissionEntity, atLeast(1)).setCompleted(Mockito.<Boolean>any());
        verify(assignmentSubmissionEntity, atLeast(1)).setGrade(Mockito.any());
        verify(assignmentSubmissionEntity).setId(eq(1L));
        verify(assignmentSubmissionEntity).setResourceIds(isA(List.class));
        verify(assignmentSubmissionEntity).setStudent_id(eq(1L));
        verify(assignmentSubmissionEntity).setSubmission_time(isA(Date.class));
        verify(assignmentSubmissionRepository).findAssignmentSubmissionEntityById(isNull());
        verify(iAssignmentSubmissionValidation).Exists(isNull());
        verify(assignmentSubmissionRepository).save(isA(AssignmentSubmissionEntity.class));
        AssignmentSubmissionResponse data = actualGradeAssignmentSubmissionResult.getData();
        assertEquals("Comment", data.getComment());
        assertNull(actualGradeAssignmentSubmissionResult.getMessageError());
        assertEquals(1L, data.getId().longValue());
        assertEquals(1L, data.getStudentID().longValue());
        assertEquals(Grade.UNDEFINED, data.getGrade());
        assertEquals(HttpStatus.CREATED, actualGradeAssignmentSubmissionResult.getHttpStatus());
        assertTrue(data.getCompleted());
        assertTrue(actualGradeAssignmentSubmissionResult.isSuccess());
        assertTrue(data.getResources().isEmpty());
    }

    
    @Test
    @DisplayName("Test gradeAssignmentSubmission(GradeAssignmentSubmissionRequest); then return Data Grade is 'null'")
    
    @MethodsUnderTest({
            "ServiceResult com.lms.assignment_service.services.AssignmentSubmissionService.gradeAssignmentSubmission(GradeAssignmentSubmissionRequest)"})
    void testGradeAssignmentSubmission_thenReturnDataGradeIsNull() {
        // Arrange
        AssignmentEntity assignment = new AssignmentEntity();
        assignment.setCourseId(1L);
        assignment.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignment.setDescription("The characteristics of someone or something");
        assignment.setId(1L);
        assignment.setModule_id(1L);
        assignment.setResourceIds(new ArrayList<>());
        assignment.setTitle("Dr");

        AssignmentSubmissionEntity assignmentSubmissionEntity = new AssignmentSubmissionEntity();
        assignmentSubmissionEntity.setAssignment(assignment);
        assignmentSubmissionEntity.setComment("Comment");
        assignmentSubmissionEntity.setCompleted(true);
        assignmentSubmissionEntity.setGrade(Grade.UNDEFINED);
        assignmentSubmissionEntity.setId(1L);
        assignmentSubmissionEntity.setResourceIds(new ArrayList<>());
        assignmentSubmissionEntity.setStudent_id(1L);
        assignmentSubmissionEntity
                .setSubmission_time(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        AssignmentEntity assignment2 = new AssignmentEntity();
        assignment2.setCourseId(1L);
        assignment2.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignment2.setDescription("The characteristics of someone or something");
        assignment2.setId(1L);
        assignment2.setModule_id(1L);
        assignment2.setResourceIds(new ArrayList<>());
        assignment2.setTitle("Dr");

        AssignmentSubmissionEntity assignmentSubmissionEntity2 = new AssignmentSubmissionEntity();
        assignmentSubmissionEntity2.setAssignment(assignment2);
        assignmentSubmissionEntity2.setComment("Comment");
        assignmentSubmissionEntity2.setCompleted(true);
        assignmentSubmissionEntity2.setGrade(Grade.UNDEFINED);
        assignmentSubmissionEntity2.setId(1L);
        assignmentSubmissionEntity2.setResourceIds(new ArrayList<>());
        assignmentSubmissionEntity2.setStudent_id(1L);
        assignmentSubmissionEntity2
                .setSubmission_time(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(assignmentSubmissionRepository.save(Mockito.any()))
                .thenReturn(assignmentSubmissionEntity2);
        when(assignmentSubmissionRepository.findAssignmentSubmissionEntityById(Mockito.<Long>any()))
                .thenReturn(assignmentSubmissionEntity);
        when(iAssignmentSubmissionValidation.Exists(Mockito.<Long>any())).thenReturn(true);

        // Act
        ServiceResult<AssignmentSubmissionResponse> actualGradeAssignmentSubmissionResult = assignmentSubmissionService
                .gradeAssignmentSubmission(new GradeAssignmentSubmissionRequest());

        // Assert
        verify(assignmentSubmissionRepository).findAssignmentSubmissionEntityById(isNull());
        verify(iAssignmentSubmissionValidation).Exists(isNull());
        verify(assignmentSubmissionRepository).save(isA(AssignmentSubmissionEntity.class));
        AssignmentSubmissionResponse data = actualGradeAssignmentSubmissionResult.getData();
        assertNull(data.getGrade());
        assertNull(data.getCompleted());
        assertNull(data.getComment());
        assertNull(actualGradeAssignmentSubmissionResult.getMessageError());
        assertEquals(1L, data.getId().longValue());
        assertEquals(1L, data.getStudentID().longValue());
        assertEquals(HttpStatus.CREATED, actualGradeAssignmentSubmissionResult.getHttpStatus());
        assertTrue(actualGradeAssignmentSubmissionResult.isSuccess());
        assertTrue(data.getResources().isEmpty());
    }
}
