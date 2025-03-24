package com.lms.assignment_service.entities;

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

    @Column(nullable = false)
    private Long student_id;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private AssignmentEntity assignment;

    @Column(nullable = false)
    private Date submission_time;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private Boolean completed;

    @Column
    private String comment;

    @ElementCollection
    @CollectionTable(name = "submission_resource_ids", joinColumns = @JoinColumn(name = "assignment_aubmission_id"))
    @Column(name = "resource_id")
    private List<Long> resourceIds;
}