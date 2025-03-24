package com.lms.auth_service.entities.relational;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "instructors")
public class InstructorEntity extends UserEntity {
    @Column(name = "validated_by_admin", nullable = false)
    private boolean validatedByAdmin;
}
