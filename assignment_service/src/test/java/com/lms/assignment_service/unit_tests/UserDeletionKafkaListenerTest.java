package com.lms.assignment_service.unit_tests;

import com.lms.assignment_service.kafka.UserDeletionKafkaListener;
import com.lms.assignment_service.repositories.AssignmentSubmissionRepository;
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
public class UserDeletionKafkaListenerTest {
    @Autowired
    private UserDeletionKafkaListener userDeletionKafkaListener;

    @MockitoBean
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    @Test
    @DisplayName("onUserDeleted removes submissions")
    void testOnUserDeleted() {
        userDeletionKafkaListener.onUserDeleted(new UserDeletedEvent(1L));
        Mockito.verify(assignmentSubmissionRepository).deleteAllByStudentId(1L);
    }
}
