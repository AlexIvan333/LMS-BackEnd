package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
