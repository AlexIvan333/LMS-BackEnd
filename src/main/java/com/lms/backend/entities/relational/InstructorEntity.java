package com.lms.backend.entities.relational;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@AllArgsConstructor
@SuperBuilder
@Data
@NoArgsConstructor
@Entity
@Table(name = "instructors")
public class InstructorEntity extends UserEntity {
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseEntity> createdCourses;

    @Column(name = "validated_by_admin", nullable = false)
    private boolean validatedByAdmin;

}
