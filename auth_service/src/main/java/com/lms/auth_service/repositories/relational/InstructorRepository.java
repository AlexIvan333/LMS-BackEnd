package com.lms.auth_service.repositories.relational;

import com.lms.auth_service.entities.relational.InstructorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface InstructorRepository extends JpaRepository<InstructorEntity, Long> {

    InstructorEntity getInstructorEntityById(Long id);

    Long getInstructorEntityByEmail(String email);

    @Query("SELECT i FROM InstructorEntity i " +
            "WHERE (:instructorID IS NULL OR i.id = :instructorID) " +
            "AND (:active IS NULL OR i.active = :active)")
    Page<InstructorEntity> findInstructorEntitiesByFilters(Long instructorID,
                                                           Boolean active,
                                                           Pageable pageable);
}
