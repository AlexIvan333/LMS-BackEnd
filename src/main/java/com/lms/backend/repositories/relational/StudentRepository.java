package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}
