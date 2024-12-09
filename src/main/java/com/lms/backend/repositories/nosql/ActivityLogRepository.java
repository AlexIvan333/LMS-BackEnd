package com.lms.backend.repositories.nosql;

import com.lms.backend.entities.nosql.ActivityLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface ActivityLogRepository extends MongoRepository<ActivityLogEntity, String> {
}
