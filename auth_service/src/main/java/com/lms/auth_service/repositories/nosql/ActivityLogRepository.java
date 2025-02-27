package com.lms.auth_service.repositories.nosql;

import com.lms.auth_service.entities.nosql.ActivityLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityLogRepository extends MongoRepository<ActivityLogEntity, String> {
}
