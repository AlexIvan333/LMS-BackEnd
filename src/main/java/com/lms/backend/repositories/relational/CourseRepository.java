package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.CourseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository  extends JpaRepository<CourseEntity, Long> {

    @Query("SELECT c FROM CourseEntity c " +
            "WHERE (:courseId IS NULL OR c.id = :courseId) " +
            "AND (:instructorId IS NULL OR c.instructor.id = :instructorId) " +
            "AND (:studentId IS NULL OR EXISTS (SELECT s FROM c.enrolledStudents s WHERE s.id = :studentId)) " +
            "AND (:moduleId IS NULL OR EXISTS (SELECT m FROM c.modules m WHERE m.id = :moduleId)) " +
            "AND (:maxStudents IS NULL OR c.maxStudents <= :maxStudents)")
    Page<CourseEntity> findCourseEntitiesByFilters(Long courseId,
                                                   Long instructorId,
                                                   Long studentId,
                                                   Long moduleId,
                                                   Integer maxStudents,
                                                   Pageable pageable);

    CourseEntity findCourseEntitiesById(Long id);
}
