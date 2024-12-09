package com.lms.backend.repositories.relational;

import com.lms.backend.entities.relational.CertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<CertificateEntity, Long> {
}
