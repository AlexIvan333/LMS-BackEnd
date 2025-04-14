package com.lms.auth_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.lms.auth_service.kafka.AuthKafkaListener;
import com.lms.auth_service.repositories.relational.UserRepository;
import com.lms.shared.events.CheckUserExistsEvent;
import com.lms.shared.events.UserExistsResponseEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthKafkaListener.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AuthKafkaListenerTest {
    @Autowired
    private AuthKafkaListener authKafkaListener;

    @MockitoBean
    private UserRepository userRepository;


    @Test
    @DisplayName("Test onUserValidationRequest(CheckUserExistsEvent); then return userId longValue is one")
    
    @MethodsUnderTest({"UserExistsResponseEvent AuthKafkaListener.onUserValidationRequest(CheckUserExistsEvent)"})
    void testOnUserValidationRequest_thenReturnUserIdLongValueIsOne() {
        // Arrange and Act
        UserExistsResponseEvent actualOnUserValidationRequestResult = authKafkaListener
                .onUserValidationRequest(new CheckUserExistsEvent(1L, "Role", "42"));

        // Assert
        assertEquals("42", actualOnUserValidationRequestResult.correlationId());
        assertEquals(1L, actualOnUserValidationRequestResult.userId().longValue());
        assertFalse(actualOnUserValidationRequestResult.exists());
    }


    @Test
    @DisplayName("Test onUserValidationRequest(CheckUserExistsEvent); then return userId longValue is two")
    
    @MethodsUnderTest({"UserExistsResponseEvent AuthKafkaListener.onUserValidationRequest(CheckUserExistsEvent)"})
    void testOnUserValidationRequest_thenReturnUserIdLongValueIsTwo() {
        // Arrange and Act
        UserExistsResponseEvent actualOnUserValidationRequestResult = authKafkaListener
                .onUserValidationRequest(new CheckUserExistsEvent(2L, "Role", "42"));

        // Assert
        assertEquals("42", actualOnUserValidationRequestResult.correlationId());
        assertEquals(2L, actualOnUserValidationRequestResult.userId().longValue());
        assertFalse(actualOnUserValidationRequestResult.exists());
    }
}
