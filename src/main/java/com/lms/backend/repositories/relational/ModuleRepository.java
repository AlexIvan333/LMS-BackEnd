package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {
}
