package com.lms.resource_service.configuration.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userValidationRequestTopic() {
        return new NewTopic("user-validation-request", 1, (short) 1);
    }

    @Bean
    public NewTopic userValidationResponseTopic() {
        return new NewTopic("user-validation-response", 1, (short) 1);
    }

    @Bean
    public NewTopic resourceValidationRequestTopic() {
        return new NewTopic("resource-validation-request", 1, (short) 1);
    }

    @Bean
    public NewTopic resourceValidationResponseTopic() {
        return new NewTopic("resource-validation-response", 1, (short) 1);
    }
}
