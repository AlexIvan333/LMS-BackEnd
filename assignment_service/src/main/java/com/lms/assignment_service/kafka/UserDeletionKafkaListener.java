package com.lms.assignment_service.kafka;
import com.lms.assignment_service.repositories.AssignmentSubmissionRepository;
import com.lms.shared.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserDeletionKafkaListener {
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;

    @KafkaListener(topics = "user-deleted", groupId = "assignment-service")
    @Transactional
    public void onUserDeleted(UserDeletedEvent event) {
        assignmentSubmissionRepository.deleteAllByStudentId(event.userId());
    }
}
