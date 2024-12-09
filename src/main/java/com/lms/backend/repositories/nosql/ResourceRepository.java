package com.lms.backend.repositories.nosql;

import com.lms.backend.entities.nosql.ResourceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceRepository extends MongoRepository<ResourceEntity, String> {
}
