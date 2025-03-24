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
@Table(name = "assignments")
public class AssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date deadline;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private Long module_id;

    @ElementCollection
    @CollectionTable(name = "assignment_resource_ids", joinColumns = @JoinColumn(name = "assignment_id"))
    @Column(name = "resource_id")
    private List<Long> resourceIds;

}
