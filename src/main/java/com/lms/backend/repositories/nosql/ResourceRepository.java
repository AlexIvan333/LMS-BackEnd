package com.lms.backend.repositories.nosql;

import com.lms.backend.entities.nosql.ResourceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResourceRepository extends MongoRepository<ResourceEntity, Long> {

    ResourceEntity findResourceById(Long id);

    Boolean existsResourceById(Long id);


}
