package com.lms.auth_service.repositories.relational;

import com.lms.auth_service.entities.relational.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    StudentEntity findStudentEntityById(Long id);

    @Query("SELECT s FROM StudentEntity s " +
            "WHERE (:studentId IS NULL OR s.id = :studentId) " +
            "AND (:active IS NULL OR s.active = :active)" +
            "AND (:email IS NULL OR s.email = :email) ")
    Page<StudentEntity> findStudentEntitiesByFilters(Long studentId,
                                                     Boolean active,
                                                     String email,
                                                     Pageable pageable);
}
