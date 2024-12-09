package com.lms.backend.entities.nosql;

import com.lms.backend.domain.enums.ResourceType;
import com.lms.backend.entities.relational.ModuleEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "resources")
public class ResourceEntity {
    @Id
    private String id;

    private String title;

    private ResourceType type;

    private String url;

    private Long moduleId;
}
