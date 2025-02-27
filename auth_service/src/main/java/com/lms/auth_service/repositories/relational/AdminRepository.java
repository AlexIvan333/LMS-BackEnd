package com.lms.auth_service.repositories.relational;

import com.lms.auth_service.entities.relational.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
}
