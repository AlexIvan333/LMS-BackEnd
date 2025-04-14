package com.lms.resource_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lms.resource_service.kafka.ResourceKafkaListener;
import com.lms.resource_service.repositories.ResourceRepository;
import com.lms.shared.events.CheckResourceExistsEvent;
import com.lms.shared.events.ResourceExistsResponseEvent;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ResourceKafkaListener.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ResourceKafkaListenerTest {
    @Autowired
    private ResourceKafkaListener resourceKafkaListener;

    @MockitoBean
    private ResourceRepository resourceRepository;


    @Test
    @DisplayName("Test onResourceValidation(CheckResourceExistsEvent); then return correlationId is '42'")
    void testOnResourceValidation_thenReturnCorrelationIdIs42() {
        // Arrange
        when(resourceRepository.findAllById(Mockito.any())).thenReturn(new ArrayList<>());

        // Act
        ResourceExistsResponseEvent actualOnResourceValidationResult = resourceKafkaListener
                .onResourceValidation(new CheckResourceExistsEvent(new ArrayList<>(), "42"));

        // Assert
        verify(resourceRepository).findAllById(isA(Iterable.class));
        assertEquals("42", actualOnResourceValidationResult.correlationId());
        assertTrue(actualOnResourceValidationResult.validResourceIds().isEmpty());
    }
}
