package com.lms.assignment_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.assignment_service.dtos.responses.AssignmentResponse;
import com.lms.assignment_service.dtos.responses.AssignmentSubmissionResponse;
import com.lms.assignment_service.entities.AssignmentEntity;
import com.lms.assignment_service.entities.AssignmentSubmissionEntity;
import com.lms.assignment_service.entities.Grade;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;

import com.lms.assignment_service.mappers.AssignmentSubmissionMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AssignmentSubmissionMapperTest {
    
    @Test
    @DisplayName("Test toResponse(AssignmentSubmissionEntity); given 'null'; then return 'Comment'")
    
    @MethodsUnderTest({"AssignmentSubmissionResponse AssignmentSubmissionMapper.toResponse(AssignmentSubmissionEntity)"})
    void testToResponse_givenNull_thenReturnComment() {
        // Arrange
        AssignmentSubmissionEntity entity = new AssignmentSubmissionEntity();
        entity.setComment("Comment");
        entity.setCompleted(true);
        entity.setGrade(Grade.UNDEFINED);
        entity.setId(1L);
        entity.setResourceIds(new ArrayList<>());
        entity.setStudent_id(1L);
        entity.setSubmission_time(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        entity.setAssignment(null);

        // Act
        AssignmentSubmissionResponse actualToResponseResult = AssignmentSubmissionMapper.toResponse(entity);

        // Assert
        assertEquals("Comment", actualToResponseResult.getComment());
        assertNull(actualToResponseResult.getAssignmentResponse());
        assertEquals(1L, actualToResponseResult.getId().longValue());
        assertEquals(1L, actualToResponseResult.getStudentID().longValue());
        assertEquals(Grade.UNDEFINED, actualToResponseResult.getGrade());
        assertTrue(actualToResponseResult.getCompleted());
        assertTrue(actualToResponseResult.getResources().isEmpty());
    }

    
    @Test
    @DisplayName("Test toResponse(AssignmentSubmissionEntity); then return AssignmentResponse Title is 'Dr'")
    
    @MethodsUnderTest({"AssignmentSubmissionResponse AssignmentSubmissionMapper.toResponse(AssignmentSubmissionEntity)"})
    void testToResponse_thenReturnAssignmentResponseTitleIsDr() {
        // Arrange
        AssignmentEntity assignment = new AssignmentEntity();
        assignment.setCourseId(1L);
        assignment.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assignment.setDescription("The characteristics of someone or something");
        assignment.setId(1L);
        assignment.setModule_id(1L);
        assignment.setResourceIds(new ArrayList<>());
        assignment.setTitle("Dr");

        AssignmentSubmissionEntity entity = new AssignmentSubmissionEntity();
        entity.setAssignment(assignment);
        entity.setComment("Comment");
        entity.setCompleted(true);
        entity.setGrade(Grade.UNDEFINED);
        entity.setId(1L);
        entity.setResourceIds(new ArrayList<>());
        entity.setStudent_id(1L);
        entity.setSubmission_time(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        // Act and Assert
        AssignmentResponse assignmentResponse = AssignmentSubmissionMapper.toResponse(entity).getAssignmentResponse();
        assertEquals("Dr", assignmentResponse.getTitle());
        assertEquals("The characteristics of someone or something", assignmentResponse.getDescription());
        assertEquals(1L, assignmentResponse.getCourseID().longValue());
        assertEquals(1L, assignmentResponse.getId().longValue());
        assertEquals(1L, assignmentResponse.getModuleID().longValue());
        assertTrue(assignmentResponse.getResources().isEmpty());
    }

   
    @Test
    @DisplayName("Test toResponse(AssignmentSubmissionEntity); when 'null'; then return 'null'")
    
    @MethodsUnderTest({"AssignmentSubmissionResponse AssignmentSubmissionMapper.toResponse(AssignmentSubmissionEntity)"})
    void testToResponse_whenNull_thenReturnNull() {
        // Arrange, Act and Assert
        assertNull(AssignmentSubmissionMapper.toResponse(null));
    }
}
