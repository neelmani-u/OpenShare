package com.openshare.repos;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.openshare.models.FileMeta;


public interface FileMetaRepository extends MongoRepository<FileMeta, String> {
    List<FileMeta> findByFolderId(String folderId);
}
