package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.InstructorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InstructorRepository extends JpaRepository<InstructorEntity, Long> {

    @Query("SELECT i FROM InstructorEntity i " +
            "WHERE (:instructorID IS NULL OR i.id = :instructorID) " +
            "AND (:active IS NULL OR i.active = :active) " +
            "AND (:courseID IS NULL OR EXISTS (SELECT c FROM i.createdCourses c WHERE c.id = :courseID))")
    Page<InstructorEntity> findInstructorEntitiesByFilters(Long instructorID,
                                                           Boolean active,
                                                           Long courseID,
                                                           Pageable pageable);

    InstructorEntity getInstructorEntityById(Long id);

    Long getInstructorEntityByEmail(String email);
}
