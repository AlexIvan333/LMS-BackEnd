package com.lms.course_service.unit_tests;

import com.lms.course_service.kafka.UserDeletionKafkaListener;
import com.lms.course_service.repositories.CourseRepository;
import com.lms.course_service.repositories.CourseStudentRepository;
import com.lms.shared.events.UserDeletedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserDeletionKafkaListener.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserDeletionKafkaListenerTest {

    @Autowired
    private UserDeletionKafkaListener userDeletionKafkaListener;

    @MockitoBean
    private CourseRepository courseRepository;

    @MockitoBean
    private CourseStudentRepository courseStudentRepository;

    @Test
    @DisplayName("onUserDeleted removes course data")
    void testOnUserDeleted() {
        userDeletionKafkaListener.onUserDeleted(new UserDeletedEvent(1L));
        Mockito.verify(courseRepository).deleteAllByInstructorId(1L);
        Mockito.verify(courseStudentRepository).deleteAllByStudentId(1L);
    }
}