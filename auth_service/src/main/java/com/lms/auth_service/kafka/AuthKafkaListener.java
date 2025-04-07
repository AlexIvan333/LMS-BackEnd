package com.lms.auth_service.kafka;

import com.lms.auth_service.entities.enums.Role;
import com.lms.auth_service.repositories.relational.UserRepository;
import com.lms.shared.events.CheckUserExistsEvent;
import com.lms.shared.events.UserExistsResponseEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthKafkaListener {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "user-validation-request", groupId = "auth-service")
    @SendTo("user-validation-response")
    public UserExistsResponseEvent onUserValidationRequest(CheckUserExistsEvent event) {
        System.out.println("[AUTH SERVICE] Received user-validation-request for ID: " + event.userId());
        boolean exists = false;
        try {
            Role role = Role.valueOf(event.role().toUpperCase());
            exists = userRepository.existsByIdAndRole(event.userId(), role);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid role provided: " + event.role());
        }
        
        return new UserExistsResponseEvent(
                event.userId(),
                exists,
                event.correlationId()
        );
    }

}
