package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
}
