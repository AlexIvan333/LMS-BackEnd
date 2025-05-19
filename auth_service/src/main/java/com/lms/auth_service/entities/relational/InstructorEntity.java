package com.lms.auth_service.entities.relational;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "instructors")
public class InstructorEntity extends UserEntity {
    @Column(name = "validated_by_admin", nullable = false)
    private boolean validatedByAdmin;
    @ElementCollection
    List<String> courseTitles;
}
