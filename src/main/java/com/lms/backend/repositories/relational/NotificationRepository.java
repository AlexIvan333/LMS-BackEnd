package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
