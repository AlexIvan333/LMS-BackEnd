package com.lms.assignment_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.shared.events.ResourceExistsResponseEvent;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {com.lms.assignment_service.validation.AssignmentValidation.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AssignmentValidationTest {
    @Autowired
    private com.lms.assignment_service.validation.AssignmentValidation assignmentValidation;

    @MockitoBean
    private ReplyingKafkaTemplate<String, Object, ResourceExistsResponseEvent> replyingKafkaTemplate;

    
    @Test
    @DisplayName("Test isValid(CreateAssignmentRequest)")
    
    @MethodsUnderTest({
            "boolean com.lms.assignment_service.validation.AssignmentValidation.isValid(com.lms.assignment_service.dtos.requests.CreateAssignmentRequest)"})
    void testIsValid() {
        // Arrange
        com.lms.assignment_service.dtos.requests.CreateAssignmentRequest.CreateAssignmentRequestBuilder courseIdResult = com.lms.assignment_service.dtos.requests.CreateAssignmentRequest
                .builder()
                .courseId(1L);
        com.lms.assignment_service.dtos.requests.CreateAssignmentRequest.CreateAssignmentRequestBuilder moduleIdResult = courseIdResult
                .deadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()))
                .description("The characteristics of someone or something")
                .moduleId(1L);
        com.lms.assignment_service.dtos.requests.CreateAssignmentRequest request = moduleIdResult
                .resourceIds(new ArrayList<>())
                .title("Dr")
                .build();

        // Act and Assert
        assertTrue(assignmentValidation.isValid(request));
    }

   
    @Test
    @DisplayName("Test isValid(CreateAssignmentRequest); given one")
    
    @MethodsUnderTest({
            "boolean com.lms.assignment_service.validation.AssignmentValidation.isValid(com.lms.assignment_service.dtos.requests.CreateAssignmentRequest)"})
    void testIsValid_givenOne() {
        // Arrange
        com.lms.assignment_service.dtos.requests.CreateAssignmentRequest.CreateAssignmentRequestBuilder courseIdResult = com.lms.assignment_service.dtos.requests.CreateAssignmentRequest
                .builder()
                .courseId(1L);
        com.lms.assignment_service.dtos.requests.CreateAssignmentRequest.CreateAssignmentRequestBuilder moduleIdResult = courseIdResult
                .deadline(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()))
                .description("The characteristics of someone or something")
                .moduleId(1L);
        com.lms.assignment_service.dtos.requests.CreateAssignmentRequest request = moduleIdResult
                .resourceIds(new ArrayList<>())
                .title("Dr")
                .build();
        request.setModuleId(1L);
        request.setResourceIds(null);

        // Act and Assert
        assertTrue(assignmentValidation.isValid(request));
    }

   
    @Test
    @DisplayName("Test isValid(CreateAssignmentRequest); when CreateAssignmentRequest(); then return 'false'")
    
    @MethodsUnderTest({
            "boolean com.lms.assignment_service.validation.AssignmentValidation.isValid(com.lms.assignment_service.dtos.requests.CreateAssignmentRequest)"})
    void testIsValid_whenCreateAssignmentRequest_thenReturnFalse() {
        // Arrange, Act and Assert
        assertFalse(assignmentValidation.isValid(new com.lms.assignment_service.dtos.requests.CreateAssignmentRequest()));
    }
}
