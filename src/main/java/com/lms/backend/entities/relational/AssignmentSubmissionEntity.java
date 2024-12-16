package com.lms.backend.entities.relational;

import com.lms.backend.domain.enums.Grade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    @Column(nullable = false)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private Grade grade;
}
