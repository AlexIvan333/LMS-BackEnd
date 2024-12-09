package com.lms.backend.configurations;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.lms.backend.repositories.nosql")
public class MongoDBConfig {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://admin:password@localhost:27017");
    }

    @Bean
    public org.springframework.data.mongodb.core.MongoTemplate mongoTemplate() {
        return new org.springframework.data.mongodb.core.MongoTemplate(mongoClient(), "lms_db2");
    }
}
