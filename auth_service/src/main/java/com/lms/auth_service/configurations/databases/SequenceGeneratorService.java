package com.lms.auth_service.configurations.databases;

import com.lms.auth_service.entities.nosql.DatabaseSequence;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SequenceGeneratorService {

    private final MongoTemplate mongoTemplate;

    public long generateSequence(String seqName) {
        // Use MongoTemplate's findAndModify method
        DatabaseSequence counter = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class
        );

        if (counter == null) {
            throw new RuntimeException("Failed to generate sequence for: " + seqName);
        }

        return counter.getSeq();
    }
}
