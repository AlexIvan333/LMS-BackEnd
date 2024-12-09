package com.lms.backend.domain.nosql;

import com.lms.backend.domain.relational.User;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class DiscussionThread {
    private Long id;
    private String title;
    private String content;
    private Long authorID;
    private Date createdAt;
    private List<Long> commentIds;
}
