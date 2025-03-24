package com.lms.course_service.repositories;

import com.lms.course_service.entities.CourseStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseStudentRepository extends JpaRepository<CourseStudentEntity, Long> {
}
