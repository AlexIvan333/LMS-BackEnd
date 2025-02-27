package com.lms.backend.repositories.nosql;

import com.lms.backend.entities.nosql.ActivityLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityLogRepository extends MongoRepository<ActivityLogEntity, String> {
}
