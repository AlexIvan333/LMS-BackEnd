package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.AssignmentSubmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmissionEntity, Long> {
}
