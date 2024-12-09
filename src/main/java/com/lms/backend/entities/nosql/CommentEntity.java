package com.lms.backend.entities.nosql;

import com.lms.backend.entities.relational.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "comments")
public class CommentEntity {
    @Id
    private String id;

    private String content;

    private Long authorId;

    private String threadId;

    private Date createdAt;

}
