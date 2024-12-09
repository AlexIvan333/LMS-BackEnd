package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {
}
