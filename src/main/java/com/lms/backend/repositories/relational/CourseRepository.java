package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository  extends JpaRepository<CourseEntity, Long> {
}
