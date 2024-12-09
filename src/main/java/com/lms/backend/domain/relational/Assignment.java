package com.lms.backend.domain.relational;

import lombok.Data;

import java.util.Date;
@Data
public class Assignment {
    private Long id;
    private String title;
    private String description;
    private Date deadline;
    private Long moduleID;
    private Module module;
}
