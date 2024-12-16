package com.lms.backend.entities.nosql;

import com.lms.backend.entities.relational.UserEntity;
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
@Document(collection = "comments")
public class CommentEntity {
    @Id
    private String id;

    private String content;

    private Long authorId;

    private String threadId;

    private Date createdAt;

}
