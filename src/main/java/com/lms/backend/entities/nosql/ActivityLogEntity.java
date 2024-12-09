package com.lms.backend.entities.nosql;

import com.lms.backend.entities.relational.AdminEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "activity_logs")
public class ActivityLogEntity {
    @Id
    private String id;

    private Long adminId;

    private String action;

    private Date timestamp;
}
