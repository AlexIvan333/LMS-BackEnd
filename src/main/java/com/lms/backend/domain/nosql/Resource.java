package com.lms.backend.domain.nosql;

import com.lms.backend.domain.enums.ResourceType;
import com.lms.backend.domain.relational.Module;
import lombok.Data;

@Data
public class Resource {
    private Long id;
    private String title;
    private ResourceType type;
    private String url;
    private Long moduleID;
}
