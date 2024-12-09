package com.lms.backend.domain.relational;

import com.lms.backend.domain.nosql.Resource;
import lombok.Data;
import org.hibernate.sql.ast.tree.update.Assignment;

import java.util.List;

@Data
public class Module {
    private Long id;
    private String title;
    private String description;
    private List<Assignment> assignments;
    private List<Long> resourceIds;
}
