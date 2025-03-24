package com.lms.assignment_service.repositories;



import com.lms.assignment_service.entities.AssignmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {

    @Query("SELECT a FROM AssignmentEntity a " +
            "WHERE (:assignmentId IS NULL OR a.id = :assignmentId) " +
            "AND (:moduleId IS NULL OR a.module_id = :moduleId)" +
            "AND (:courseId IS NULL OR a.courseId = :courseId)")
    Page<AssignmentEntity> findAssignmentEntitiesByFilters(Long assignmentId,
                                                           Long courseId,
                                                           Long moduleId,
                                                           Pageable pageable);

    AssignmentEntity findAssignmentEntitiesById(Long id);
}
