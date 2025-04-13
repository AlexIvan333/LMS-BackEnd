package com.lms.auth_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.auth_service.dtos.responses.StudentResponse;
import com.lms.auth_service.entities.enums.Role;
import com.lms.auth_service.entities.relational.StudentEntity;
import com.lms.auth_service.mappers.StudentMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StudentMapperTest {
    
    @Test
    @DisplayName("Test toResponse(StudentEntity); given 'true'; when StudentEntity() Active is 'true'; then return ZipCode is '21654'")
    
    @MethodsUnderTest({"StudentResponse StudentMapper.toResponse(StudentEntity)"})
    void testToResponse_givenTrue_whenStudentEntityActiveIsTrue_thenReturnZipCodeIs21654() {
        // Arrange
        var entity = getStudentEntity();

        // Act
        StudentResponse actualToResponseResult = StudentMapper.toResponse(entity);

        // Assert
        assertEquals("21654", actualToResponseResult.getZipCode());
        assertEquals("Doe", actualToResponseResult.getLastName());
        assertEquals("GB", actualToResponseResult.getCountry());
        assertEquals("Jane", actualToResponseResult.getFirstName());
        assertEquals("Middle Name", actualToResponseResult.getMiddleName());
        assertEquals("Oxford", actualToResponseResult.getCity());
        assertEquals("Street Name", actualToResponseResult.getStreetName());
        assertEquals("jane.doe@example.org", actualToResponseResult.getEmail());
        assertEquals(10, actualToResponseResult.getStreetNumber());
        assertEquals(1L, actualToResponseResult.getId().longValue());
        assertEquals(Role.STUDENT, actualToResponseResult.getRole());
        assertTrue(actualToResponseResult.getActive());
    }

    private static StudentEntity getStudentEntity() {
        StudentEntity entity = new StudentEntity();
        entity.setActive(true);
        entity.setCity("Oxford");
        entity.setCodeForgotPassword("password");
        entity.setCountry("GB");
        entity.setEmail("jane.doe@example.org");
        entity.setFirstName("Jane");
        entity.setId(1L);
        entity.setLastName("Doe");
        entity.setMiddleName("Middle Name");
        entity.setPassword("password");
        entity.setRole(Role.STUDENT);
        entity.setStreetName("Street Name");
        entity.setStreetNumber(10);
        entity.setTwoFactorCode(3);
        entity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        entity.setZipCode("21654");
        return entity;
    }


    @Test
    @DisplayName("Test toResponse(StudentEntity); when 'null'; then return 'null'")
    
    @MethodsUnderTest({"StudentResponse StudentMapper.toResponse(StudentEntity)"})
    void testToResponse_whenNull_thenReturnNull() {
        // Arrange, Act and Assert
        assertNull(StudentMapper.toResponse(null));
    }
}
