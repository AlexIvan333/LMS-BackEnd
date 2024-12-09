package com.lms.backend.domain.relational;

import lombok.Data;

import java.util.Date;
@Data
public class Notification {
    private Long id;
    private String message;
    private Date timestamp;
    private boolean isRead;
    private Long recipientID;
    private User recipient;
}
