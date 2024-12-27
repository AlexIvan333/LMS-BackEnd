package com.lms.backend.entities.relational;

import com.lms.backend.entities.nosql.ResourceEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "assignments")
public class AssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date deadline;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private ModuleEntity module;

    @ElementCollection
    @CollectionTable(name = "assignment_resource_ids", joinColumns = @JoinColumn(name = "assignment_id"))
    @Column(name = "resource_id")
    private List<Long> resourceIds;

    @Transient
    private List<ResourceEntity> resources;
}
