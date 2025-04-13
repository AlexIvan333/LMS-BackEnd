package com.lms.course_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lms.course_service.dtos.responses.CourseResponse;
import com.lms.course_service.entities.CourseEntity;

import java.util.ArrayList;

import com.lms.course_service.mappers.CourseMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CourseMapperTest {

    @Test
    @DisplayName("Test toResponse(CourseEntity); given 'The characteristics of someone or something'; then return Title is 'Dr'")
    
    void testToResponse_givenTheCharacteristicsOfSomeoneOrSomething_thenReturnTitleIsDr() {
        // Arrange
        CourseEntity entity = new CourseEntity();
        entity.setDescription("The characteristics of someone or something");
        entity.setId(1L);
        entity.setInstructorId(1L);
        entity.setMaxStudents(3);
        entity.setModules(new ArrayList<>());
        entity.setTitle("Dr");

        // Act
        CourseResponse actualToResponseResult = CourseMapper.toResponse(entity);

        // Assert
        assertEquals("Dr", actualToResponseResult.getTitle());
        assertEquals("The characteristics of someone or something", actualToResponseResult.getDescription());
        assertEquals(1L, actualToResponseResult.getId().longValue());
        assertEquals(1L, actualToResponseResult.getInstructorID().longValue());
        assertEquals(3, actualToResponseResult.getMaxStudents());
        assertTrue(actualToResponseResult.getModules().isEmpty());
    }

    @Test
    @DisplayName("Test toResponse(CourseEntity); when 'null'; then return 'null'")
    
    void testToResponse_whenNull_thenReturnNull() {
        // Arrange, Act and Assert
        assertNull(CourseMapper.toResponse(null));
    }
}
