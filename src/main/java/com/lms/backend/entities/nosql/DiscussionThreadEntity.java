package com.lms.backend.entities.nosql;
import com.lms.backend.entities.relational.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor

@Document(collection = "discussion_threads")
public class DiscussionThreadEntity {
    @Id
    private String id;

    private String title;

    private String content;

    private Long authorId;

    private Date createdAt;

    private List<String> commentIds;
}
