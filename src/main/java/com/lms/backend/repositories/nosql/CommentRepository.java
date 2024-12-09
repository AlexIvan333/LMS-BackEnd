package com.lms.backend.repositories.nosql;

import com.lms.backend.entities.nosql.CommentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends MongoRepository<CommentEntity, String> {
}
