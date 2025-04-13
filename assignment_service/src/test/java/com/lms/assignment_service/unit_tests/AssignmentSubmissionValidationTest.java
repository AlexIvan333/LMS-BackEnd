package com.lms.assignment_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.assignment_service.repositories.AssignmentRepository;
import com.lms.assignment_service.repositories.AssignmentSubmissionRepository;
import com.lms.shared.events.ResourceExistsResponseEvent;
import com.lms.shared.events.UserExistsResponseEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {com.lms.assignment_service.validation.AssignmentSubmissionValidation.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AssignmentSubmissionValidationTest {
    @MockitoBean
    private AssignmentRepository assignmentRepository;

    @MockitoBean
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    @Autowired
    private com.lms.assignment_service.validation.AssignmentSubmissionValidation assignmentSubmissionValidation;

    @MockitoBean
    private ReplyingKafkaTemplate<String, Object, UserExistsResponseEvent> replyingKafkaTemplate;

    @MockitoBean
    private ReplyingKafkaTemplate<String, Object, ResourceExistsResponseEvent> replyingKafkaTemplate2;

    
    @Test
    @DisplayName("Test isValid(CreateAssignmentSubmissionRequest)")
    
    @MethodsUnderTest({
            "boolean com.lms.assignment_service.validation.AssignmentSubmissionValidation.isValid(com.lms.assignment_service.dtos.requests.CreateAssignmentSubmissionRequest)"})
    void testIsValid() {
        // Arrange, Act and Assert
        assertFalse(assignmentSubmissionValidation
                .isValid(new com.lms.assignment_service.dtos.requests.CreateAssignmentSubmissionRequest()));
    }

    
    @Test
    @DisplayName("Test Exists(Long); then return 'false'")
    
    @MethodsUnderTest({"boolean com.lms.assignment_service.validation.AssignmentSubmissionValidation.Exists(Long)"})
    void testExists_thenReturnFalse() {
        // Arrange
        when(assignmentSubmissionRepository.existsById(Mockito.<Long>any())).thenReturn(false);

        // Act
        boolean actualExistsResult = assignmentSubmissionValidation.Exists(1L);

        // Assert
        verify(assignmentSubmissionRepository).existsById(eq(1L));
        assertFalse(actualExistsResult);
    }

    
    @Test
    @DisplayName("Test Exists(Long); then return 'true'")
    
    @MethodsUnderTest({"boolean com.lms.assignment_service.validation.AssignmentSubmissionValidation.Exists(Long)"})
    void testExists_thenReturnTrue() {
        // Arrange
        when(assignmentSubmissionRepository.existsById(Mockito.<Long>any())).thenReturn(true);

        // Act
        boolean actualExistsResult = assignmentSubmissionValidation.Exists(1L);

        // Assert
        verify(assignmentSubmissionRepository).existsById(eq(1L));
        assertTrue(actualExistsResult);
    }
}
