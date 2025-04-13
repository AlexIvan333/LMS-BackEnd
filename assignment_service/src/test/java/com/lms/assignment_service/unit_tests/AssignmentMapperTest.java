package com.lms.assignment_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.assignment_service.dtos.responses.AssignmentResponse;
import com.lms.assignment_service.entities.AssignmentEntity;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;

import com.lms.assignment_service.mappers.AssignmentMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AssignmentMapperTest {
    
    @Test
    @DisplayName("Test toResponse(AssignmentEntity); given one; when AssignmentEntity() CourseId is one; then return Title is 'Dr'")
    
    @MethodsUnderTest({"AssignmentResponse AssignmentMapper.toResponse(AssignmentEntity)"})
    void testToResponse_givenOne_whenAssignmentEntityCourseIdIsOne_thenReturnTitleIsDr() {
        // Arrange
        AssignmentEntity entity = new AssignmentEntity();
        entity.setCourseId(1L);
        entity.setDeadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        entity.setDescription("The characteristics of someone or something");
        entity.setId(1L);
        entity.setModule_id(1L);
        entity.setResourceIds(new ArrayList<>());
        entity.setTitle("Dr");

        // Act
        AssignmentResponse actualToResponseResult = AssignmentMapper.toResponse(entity);

        // Assert
        assertEquals("Dr", actualToResponseResult.getTitle());
        assertEquals("The characteristics of someone or something", actualToResponseResult.getDescription());
        assertEquals(1L, actualToResponseResult.getCourseID().longValue());
        assertEquals(1L, actualToResponseResult.getId().longValue());
        assertEquals(1L, actualToResponseResult.getModuleID().longValue());
        assertTrue(actualToResponseResult.getResources().isEmpty());
    }

    
    @Test
    @DisplayName("Test toResponse(AssignmentEntity); when 'null'; then return 'null'")
    
    @MethodsUnderTest({"AssignmentResponse AssignmentMapper.toResponse(AssignmentEntity)"})
    void testToResponse_whenNull_thenReturnNull() {
        // Arrange, Act and Assert
        assertNull(AssignmentMapper.toResponse(null));
    }
}
