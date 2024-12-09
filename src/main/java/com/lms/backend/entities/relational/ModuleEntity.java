package com.lms.backend.entities.relational;

import com.lms.backend.entities.nosql.ResourceEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
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
