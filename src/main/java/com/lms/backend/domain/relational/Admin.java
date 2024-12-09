package com.lms.backend.domain.relational;

import com.lms.backend.domain.nosql.ActivityLog;
import lombok.Data;

import java.util.List;

@Data
public class Admin extends User {
    private String department;
    private List<Long> activityLogIds;
}
