package com.lms.backend.domain.relational;

import lombok.Data;

import java.util.Date;
@Data
public class Announcement {
    private Long id;
    private String title;
    private String content;
    private Date createdAt;
    private Long authorID;
    private User author;
    private Long courseID;
    private Course course;
}
