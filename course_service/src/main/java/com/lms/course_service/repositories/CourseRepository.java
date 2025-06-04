package com.lms.course_service.repositories;


import com.lms.course_service.entities.CourseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    @Query("SELECT c FROM CourseEntity c " +
            "WHERE (:courseId IS NULL OR c.id = :courseId) " +
            "AND (:instructorId IS NULL OR c.instructorId = :instructorId) " +
            "AND (:moduleId IS NULL OR EXISTS (SELECT m FROM c.modules m WHERE m.id = :moduleId)) " +
            "AND (:maxStudents IS NULL OR c.maxStudents <= :maxStudents)")
    Page<CourseEntity> findCourseEntitiesByFilters(Long courseId,
                                                   Long instructorId,
                                                   Long moduleId,
                                                   Integer maxStudents,
                                                   Pageable pageable);

    Optional<CourseEntity> findCourseEntitiesById(Long id);
    void deleteAllByInstructorId(Long instructorId);

    java.util.List<CourseEntity> findAllByInstructorId(Long instructorId);
}
