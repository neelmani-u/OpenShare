package com.openshare.repos;


import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.openshare.models.Folder;

public interface FolderRepository extends MongoRepository<Folder, String> {
    List<Folder> findByOwnerId(String ownerId);
    List<Folder> findBySharedWithContaining(String userId);
}

