package com.lms.auth_service.repositories.relational;

import com.lms.auth_service.entities.relational.InstructorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface InstructorRepository extends JpaRepository<InstructorEntity, Long> {

    InstructorEntity getInstructorEntityById(Long id);

    @Query("SELECT i FROM InstructorEntity i LEFT JOIN FETCH i.courseTitles WHERE i.id = :id")
    Optional<InstructorEntity> findByIdWithCourseTitles(Long id);

    Long getInstructorEntityByEmail(String email);

    @Query("SELECT i FROM InstructorEntity i " +
            "WHERE (:instructorID IS NULL OR i.id = :instructorID) " +
            "AND (:active IS NULL OR i.active = :active)" +
            "AND (:email IS NULL OR i.email = :email) ")
    Page<InstructorEntity> findInstructorEntitiesByFilters(Long instructorID,
                                                           Boolean active,
                                                           String email,
                                                           Pageable pageable);
}
