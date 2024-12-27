package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.AssignmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {

    @Query("SELECT a FROM AssignmentEntity a " +
            "WHERE (:assignmentId IS NULL OR a.id = :assignmentId) " +
            "AND (:courseId IS NULL OR EXISTS (" +
            "    SELECT m FROM ModuleEntity m WHERE m.id = a.module.id AND m.course.id = :courseId" +
            ")) " +
            "AND (:moduleId IS NULL OR a.module.id = :moduleId)")
    Page<AssignmentEntity> findAssignmentEntitiesByFilters(Long assignmentId,
                                                           Long courseId,
                                                           Long moduleId,
                                                           Pageable pageable);

    AssignmentEntity findAssignmentEntitiesById(Long id);
}
