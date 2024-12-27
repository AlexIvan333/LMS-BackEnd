package com.lms.backend.entities.relational;

import com.lms.backend.domain.enums.Grade;
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
@Table(name = "assignment_submissions")
public class AssignmentSubmissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private AssignmentEntity assignment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date submissionTime;

    @ElementCollection
    @CollectionTable(name = "assignment_submission_resources", joinColumns = @JoinColumn(name = "submission_id"))
    @Column(name = "resource_id")
    private List<Long> resourceIds;

    @Transient // Mark as transient because it is not stored in the relational database
    private List<ResourceEntity> resources;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(nullable = true)
    private Boolean completed;

    @Column
    private String comment;
}
