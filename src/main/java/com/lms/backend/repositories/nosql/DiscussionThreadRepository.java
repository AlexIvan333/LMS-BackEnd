package com.lms.backend.repositories.nosql;

import com.lms.backend.entities.nosql.DiscussionThreadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiscussionThreadRepository extends MongoRepository<DiscussionThreadEntity, String> {
}
