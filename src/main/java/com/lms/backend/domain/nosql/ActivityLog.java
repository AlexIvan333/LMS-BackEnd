package com.lms.backend.domain.nosql;

import com.lms.backend.domain.relational.Admin;
import lombok.Data;

import java.util.Date;
@Data
public class ActivityLog {
    private Long id;
    private Long adminID;
    private String action;
    private Date timestamp;
}
