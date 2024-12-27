package com.lms.backend.entities.nosql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Document(collection = "resources")
public class ResourceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title; // File name
    private String fileType; // MIME type (e.g., "application/pdf", "video/mp4")
    private String fileName; // Original file name
    private long fileSize; // File size in bytes
    private byte[] fileData; // Actual file data
}
