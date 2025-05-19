package com.lms.course_service.repositories;

import com.lms.course_service.entities.CourseStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseStudentRepository extends JpaRepository<CourseStudentEntity, Long> {
    boolean existsCourseStudentEntitiesByCourseIdAndStudentId(Long courseId, Long studentId);

    List<CourseStudentEntity> findAllByStudentId(Long studentId);
}
