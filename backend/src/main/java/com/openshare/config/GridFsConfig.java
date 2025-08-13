package com.openshare.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.MongoDatabaseFactory;

@Configuration
public class GridFsConfig {
    @Bean
    GridFsTemplate gridFsTemplate(MongoDatabaseFactory dbFactory, MongoTemplate mongoTemplate) {
        return new GridFsTemplate(dbFactory, mongoTemplate.getConverter());
    }
}

