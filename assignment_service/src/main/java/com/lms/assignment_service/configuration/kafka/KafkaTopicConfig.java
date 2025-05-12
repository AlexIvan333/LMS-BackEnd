package com.lms.assignment_service.configuration.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userValidationRequest() {
        return new NewTopic("user-validation-request", 1, (short) 1);
    }//topic

    @Bean
    public NewTopic userValidationResponse() {
        return new NewTopic("user-validation-response", 1, (short) 1);
    }

    @Bean
    public NewTopic resourceValidationRequest() {
        return new NewTopic("resource-validation-request", 1, (short) 1);
    }

    @Bean
    public NewTopic resourceValidationResponse() {
        return new NewTopic("resource-validation-response", 1, (short) 1);
    }

    @Bean
    public NewTopic resourceDetailsRequest() {
        return new NewTopic("resource-details-request", 1, (short) 1);
    }

    @Bean
    public NewTopic resourceDetailsResponse() {
        return new NewTopic("resource-details-response", 1, (short) 1);
    }
}
