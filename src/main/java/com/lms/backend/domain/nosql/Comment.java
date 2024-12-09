package com.lms.backend.domain.nosql;

import com.lms.backend.domain.relational.User;
import lombok.Data;
import java.util.Date;

@Data
public class Comment {
    private Long id;
    private String content;
    private Long authorID;
    private Date createdAt;
    private Long threadID;
}
