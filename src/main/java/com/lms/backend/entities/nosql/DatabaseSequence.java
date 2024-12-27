package com.lms.backend.entities.nosql;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "database_sequences")
public class DatabaseSequence {
    @Id
    private String id;
    private long seq;
}
