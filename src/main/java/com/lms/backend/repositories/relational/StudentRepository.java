package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    @Query("SELECT s FROM StudentEntity s " +
            "WHERE (:studentId IS NULL OR s.id = :studentId) " +
            "AND (:active IS NULL OR s.active = :active) " +
            "AND (:courseID IS NULL OR EXISTS (SELECT c FROM s.enrolledCourses c WHERE c.id = :courseID)) " +
            "AND (:submissionID IS NULL OR EXISTS (SELECT sub FROM s.submissions sub WHERE sub.id = :submissionID))")
    Page<StudentEntity> findStudentEntitiesByFilters(Long studentId,
                                                     Boolean active,
                                                     Long courseID,
                                                     Long submissionID,
                                                     Pageable pageable);

    StudentEntity findStudentEntityById(Long id);
}
