package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.ModuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {

    @Query("SELECT m FROM ModuleEntity m " +
            "WHERE (:moduleId IS NULL OR m.id = :moduleId) " +
            "AND (:courseId IS NULL OR m.course.id = :courseId) " +
            "AND (:resourceId IS NULL OR :resourceId IN (SELECT r FROM m.resourceIds r))")
    Page<ModuleEntity>findModulesByFilters(Long moduleId,
                                           Long courseId,
                                           String resourceId,
                                           Pageable pageable);
}
