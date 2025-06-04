package com.lms.course_service.kafka;

import com.lms.course_service.repositories.CourseRepository;
import com.lms.course_service.repositories.CourseStudentRepository;
import com.lms.shared.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserDeletionKafkaListener {

    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;

    @KafkaListener(topics = "user-deleted", groupId = "course-service")
    @Transactional
    public void onUserDeleted(UserDeletedEvent event) {
        courseRepository.deleteAllByInstructorId(event.userId());
        courseStudentRepository.deleteAllByStudentId(event.userId());
    }
}