package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<InstructorEntity, Long> {
}
