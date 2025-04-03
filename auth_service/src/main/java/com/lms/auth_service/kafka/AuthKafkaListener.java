package com.lms.auth_service.kafka;
import com.lms.auth_service.repositories.relational.UserRepository;
import com.lms.shared.events.CheckUserExistsEvent;
import com.lms.shared.events.UserExistsResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthKafkaListener {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "user-validation-request", groupId = "lms-group")
    public void onUserValidationRequest(CheckUserExistsEvent event) {
        boolean exists = userRepository.existsById(event.userId()); // Your auth logic
        UserExistsResponseEvent response = new UserExistsResponseEvent(event.userId(), exists, event.correlationId());
        kafkaTemplate.send("user-validation-response", response);
    }
}
