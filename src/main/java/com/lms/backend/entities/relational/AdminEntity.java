package com.lms.backend.entities.relational;

import com.lms.backend.entities.nosql.ActivityLogEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "admins")
public class AdminEntity extends UserEntity {
    @Column(nullable = false)
    private String department;

    // Remove the relationship to ActivityLogEntity and use an alternative reference if needed
    @ElementCollection
    @CollectionTable(name = "admin_activity_logs", joinColumns = @JoinColumn(name = "admin_id"))
    @Column(name = "activity_log_id")
    private List<String> activityLogIds;
}
