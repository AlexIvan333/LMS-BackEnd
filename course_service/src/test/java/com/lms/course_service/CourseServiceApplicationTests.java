package com.lms.course_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ImportAutoConfiguration(exclude = {KafkaAutoConfiguration.class})
class CourseServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
