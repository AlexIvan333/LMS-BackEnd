package com.lms.backend.entities.relational;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "modules")
public class ModuleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentEntity> assignments;

    @ElementCollection
    @CollectionTable(name = "module_resources", joinColumns = @JoinColumn(name = "module_id"))
    @Column(name = "resource_id")
    private List<String> resourceIds;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;
}
