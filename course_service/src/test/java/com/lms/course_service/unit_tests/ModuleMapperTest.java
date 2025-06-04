package com.lms.course_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lms.course_service.dtos.responses.ModuleResponse;
import com.lms.course_service.entities.CourseEntity;
import com.lms.course_service.entities.ModuleEntity;

import java.util.ArrayList;

import com.lms.course_service.mappers.ModuleMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ModuleMapperTest {

    @Test
    @DisplayName("Test toResponse(ModuleEntity); then return Title is 'Dr'")
    
    void testToResponse_thenReturnTitleIsDr() {
        // Arrange
        CourseEntity course = new CourseEntity();
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructorId(1L);
        course.setMaxStudents(3);
        course.setModules(new ArrayList<>());
        course.setTitle("Dr");

        ModuleEntity entity = new ModuleEntity();
        entity.setCourse(course);
        entity.setDescription("The characteristics of someone or something");
        entity.setId(1L);
        entity.setResourceIds(new ArrayList<>());
        entity.setTitle("Dr");

        // Act
        ModuleResponse actualToResponseResult = ModuleMapper.toResponse(entity);

        // Assert
        assertEquals("Dr", actualToResponseResult.getTitle());
        assertEquals("The characteristics of someone or something", actualToResponseResult.getDescription());
        assertEquals(1L, actualToResponseResult.getCourseId());
        assertEquals(1L, actualToResponseResult.getId().longValue());
        assertTrue(actualToResponseResult.getResources().isEmpty());
    }

    @Test
    @DisplayName("Test toResponse(ModuleEntity); when 'null'; then return 'null'")
    
    void testToResponse_whenNull_thenReturnNull() {
        // Arrange, Act and Assert
        assertNull(ModuleMapper.toResponse(null));
    }
}
