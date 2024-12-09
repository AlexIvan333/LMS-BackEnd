package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {
}
