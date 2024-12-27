package com.lms.backend.repositories.relational;

import com.lms.backend.domain.enums.Grade;
import com.lms.backend.entities.relational.AssignmentSubmissionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmissionEntity, Long> {

    @Query("SELECT s FROM AssignmentSubmissionEntity s " +
            "WHERE (:submissionId IS NULL OR s.id = :submissionId) " +
            "AND (:studentId IS NULL OR s.student.id = :studentId) " +
            "AND (:assignmentId IS NULL OR s.assignment.id = :assignmentId) " +
            "AND (:completed IS NULL OR s.completed = :completed) " +
            "AND (:grade IS NULL OR s.grade = :grade) " +
            "AND (:submissionTime IS NULL OR s.submissionTime <= :submissionTime)")
    Page<AssignmentSubmissionEntity> findAssignmentSubmissionsByFilters(Long submissionId,
                                                                        Long studentId,
                                                                        Long assignmentId,
                                                                        Boolean completed,
                                                                        Grade grade,
                                                                        Date submissionTime,
                                                                        Pageable pageable);
}
