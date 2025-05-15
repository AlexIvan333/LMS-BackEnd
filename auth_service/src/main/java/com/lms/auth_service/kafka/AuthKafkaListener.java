package com.lms.auth_service.kafka;

import com.lms.auth_service.entities.enums.Role;
import com.lms.auth_service.entities.relational.UserEntity;
import com.lms.auth_service.repositories.relational.UserRepository;
import com.lms.shared.events.CheckUserExistsEvent;
import com.lms.shared.events.UserExistsResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class AuthKafkaListener {

    private final UserRepository userRepository;

    @KafkaListener(topics = "user-validation-request", groupId = "auth-service")
    @SendTo("user-validation-response")
    public UserExistsResponseEvent onUserValidationRequest(CheckUserExistsEvent event) {
        System.out.println("[AUTH SERVICE] Received user-validation-request for ID: " + event.userId());
        boolean exists = false;
        boolean active = false;
        try {
            Role role = Role.valueOf(event.role().toUpperCase());
            exists = userRepository.existsByIdAndRole(event.userId(), role);
            active = userRepository.findById(event.userId()).get().getActive();
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid role provided: " + event.role());
        }
        
        return new UserExistsResponseEvent(
                event.userId(),
                exists,
                active,
                event.correlationId()
        );
    }

}
