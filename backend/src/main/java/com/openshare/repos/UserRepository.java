package com.openshare.repos;


import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.openshare.models.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
