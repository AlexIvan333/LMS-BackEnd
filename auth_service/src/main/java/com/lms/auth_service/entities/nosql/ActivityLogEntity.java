package com.lms.auth_service.entities.nosql;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(collection = "activity_logs")
public class ActivityLogEntity {
    @Id
    private String id;

    private Long userId;

    private String action;

    private Date timestamp;
}
