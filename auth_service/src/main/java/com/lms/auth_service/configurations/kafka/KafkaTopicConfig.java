package com.lms.auth_service.configurations.kafka;

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
    public NewTopic resourceValidationRequestTopic() { return new NewTopic("resource-validation-request", 1, (short) 1); }

    @Bean
    public NewTopic resourceValidationResponseTopic() { return new NewTopic("resource-validation-response", 1, (short) 1); }

    @Bean
    public NewTopic resourceDetailsRequestTopic() {
        return new NewTopic("resource-details-request", 1, (short) 1);
    }

    @Bean
    public NewTopic resourceDetailsResponseTopic() {
        return new NewTopic("resource-details-response", 1, (short) 1);
    }

    @Bean
    public NewTopic getInstructorDetailsRequest() { return new NewTopic("get-instructor-details-request", 1, (short) 1); }

    @Bean
    public NewTopic getInstructorDetailsResponse() { return new NewTopic("get-instructor-details-response", 1, (short) 1); }

    @Bean
    public NewTopic instructorCourseCreated() { return new NewTopic("instructor-course-created", 1, (short) 1); }

    @Bean
    public NewTopic userDeletedTopic() { return new NewTopic("user-deleted", 1, (short) 1); }
}
