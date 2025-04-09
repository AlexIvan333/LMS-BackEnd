package com.lms.auth_service.repositories.relational;

import com.lms.auth_service.entities.enums.Role;
import com.lms.auth_service.entities.relational.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    boolean existsByIdAndRole(Long id, Role role);
}
