package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.ModuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {

    @Query("SELECT m FROM ModuleEntity m " +
            "WHERE (:moduleId IS NULL OR m.id = :moduleId) " +
            "AND (:courseId IS NULL OR m.course.id = :courseId) ")
    Page<ModuleEntity> findModulesEntitiesByFilters(Long moduleId,
                                            Long courseId,
                                            Pageable pageable);


    ModuleEntity findModuleEntityById(Long id);
}
