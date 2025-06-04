package com.lms.assignment_service.repositories;


import com.lms.assignment_service.entities.AssignmentSubmissionEntity;
import com.lms.assignment_service.entities.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmissionEntity, Long> {

    @Query("SELECT s FROM AssignmentSubmissionEntity s WHERE " +
            "(:submissionId IS NULL OR s.id = :submissionId) AND " +
            "(:studentId IS NULL OR s.student_id = :studentId) AND " +
            "(:assignmentId IS NULL OR s.assignment.id = :assignmentId) AND " +
            "(:completed IS NULL OR s.completed = :completed) AND " +
            "(:grade IS NULL OR s.grade = :grade)")

    Page<AssignmentSubmissionEntity> getAssignmentSubmissionsEntitiesByFilters(
            @Param("submissionId") Long submissionId,
            @Param("studentId") Long studentId,
            @Param("assignmentId") Long assignmentId,
            @Param("completed") Boolean completed,
            @Param("grade") Grade grade,
            Pageable pageable
    );

    AssignmentSubmissionEntity findAssignmentSubmissionEntityById(Long id);

    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM AssignmentSubmissionEntity s WHERE s.student_id = :studentId")
    void deleteAllByStudentId(@Param("studentId") Long studentId);
}
