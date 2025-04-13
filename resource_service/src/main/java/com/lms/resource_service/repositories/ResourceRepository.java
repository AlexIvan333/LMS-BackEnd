package com.lms.resource_service.repositories;


import com.lms.resource_service.entites.ResourceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceRepository extends MongoRepository<ResourceEntity, Long> {

    ResourceEntity findResourceById(Long id);


    boolean existsResourceEntityById(long id);
}
