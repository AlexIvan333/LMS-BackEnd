package com.lms.backend.entities.nosql;

import com.lms.backend.entities.relational.AdminEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Document(collection = "activity_logs")
public class ActivityLogEntity {
    @Id
    private String id;

    private Long userId;

    private String action;

    private Date timestamp;
}
