package com.lms.auth_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lms.auth_service.entities.enums.Role;
import com.lms.auth_service.entities.relational.UserEntity;
import com.lms.auth_service.repositories.relational.UserRepository;

import java.util.Optional;

import com.lms.auth_service.validation.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserValidator.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserValidatorTest {
    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;
    
    @Test
    @DisplayName("Test HasValidEmail(String); given UserEntity() Active is 'true'; then return 'true'")
    void testHasValidEmail_givenUserEntityActiveIsTrue_thenReturnTrue() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setActive(true);
        userEntity.setCity("Oxford");
        userEntity.setCodeForgotPassword("password");
        userEntity.setCountry("GB");
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setFirstName("Jane");
        userEntity.setId(1L);
        userEntity.setLastName("Doe");
        userEntity.setMiddleName("Middle Name");
        userEntity.setPassword("password");
        userEntity.setRole(Role.STUDENT);
        userEntity.setStreetName("Street Name");
        userEntity.setStreetNumber(10);
        userEntity.setTwoFactorCode(3);
        userEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        userEntity.setZipCode("21654");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByEmail(Mockito.any())).thenReturn(ofResult);

        // Act
        boolean actualHasValidEmailResult = userValidator.HasValidEmail("jane.doe@example.org");

        // Assert
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        assertTrue(actualHasValidEmailResult);
    }


    @Test
    @DisplayName("Test HasValidEmail(String); given UserRepository findByEmail(String) return empty; then return 'false'")
    
    void testHasValidEmail_givenUserRepositoryFindByEmailReturnEmpty_thenReturnFalse() {
        // Arrange
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findByEmail(Mockito.any())).thenReturn(emptyResult);

        // Act
        boolean actualHasValidEmailResult = userValidator.HasValidEmail("jane.doe@example.org");

        // Assert
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        assertFalse(actualHasValidEmailResult);
    }


    @Test
    @DisplayName("Test IsIdValid(Long); given UserRepository existsById(Object) return 'false'; then return 'false'")
    
    void testIsIdValid_givenUserRepositoryExistsByIdReturnFalse_thenReturnFalse() {
        // Arrange
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(false);

        // Act
        boolean actualIsIdValidResult = userValidator.IsIdValid(1L);

        // Assert
        verify(userRepository).existsById(eq(1L));
        assertFalse(actualIsIdValidResult);
    }

    @Test
    @DisplayName("Test IsIdValid(Long); given UserRepository existsById(Object) return 'true'; then return 'true'")
    
    void testIsIdValid_givenUserRepositoryExistsByIdReturnTrue_thenReturnTrue() {
        // Arrange
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);

        // Act
        boolean actualIsIdValidResult = userValidator.IsIdValid(1L);

        // Assert
        verify(userRepository).existsById(eq(1L));
        assertTrue(actualIsIdValidResult);
    }

    @Test
    @DisplayName("Test IsInstructor(Long); given UserEntity() Role is 'INSTRUCTOR'; then return 'true'")
    
    void testIsInstructor_givenUserEntityRoleIsInstructor_thenReturnTrue() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setActive(true);
        userEntity.setCity("Oxford");
        userEntity.setCodeForgotPassword("password");
        userEntity.setCountry("GB");
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setFirstName("Jane");
        userEntity.setId(1L);
        userEntity.setLastName("Doe");
        userEntity.setMiddleName("Middle Name");
        userEntity.setPassword("password");
        userEntity.setRole(Role.INSTRUCTOR);
        userEntity.setStreetName("Street Name");
        userEntity.setStreetNumber(10);
        userEntity.setTwoFactorCode(3);
        userEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        userEntity.setZipCode("21654");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        boolean actualIsInstructorResult = userValidator.IsInstructor(1L);

        // Assert
        verify(userRepository).findById(eq(1L));
        assertTrue(actualIsInstructorResult);
    }


    @Test
    @DisplayName("Test IsInstructor(Long); given UserEntity() Role is 'STUDENT'; then return 'false'")
    
    void testIsInstructor_givenUserEntityRoleIsStudent_thenReturnFalse() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setActive(true);
        userEntity.setCity("Oxford");
        userEntity.setCodeForgotPassword("password");
        userEntity.setCountry("GB");
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setFirstName("Jane");
        userEntity.setId(1L);
        userEntity.setLastName("Doe");
        userEntity.setMiddleName("Middle Name");
        userEntity.setPassword("password");
        userEntity.setRole(Role.STUDENT);
        userEntity.setStreetName("Street Name");
        userEntity.setStreetNumber(10);
        userEntity.setTwoFactorCode(3);
        userEntity.setTwoFactorSecretKey("EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
        userEntity.setZipCode("21654");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        boolean actualIsInstructorResult = userValidator.IsInstructor(1L);

        // Assert
        verify(userRepository).findById(eq(1L));
        assertFalse(actualIsInstructorResult);
    }
}
