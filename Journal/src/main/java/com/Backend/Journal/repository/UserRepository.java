package com.Backend.Journal.repository;

import com.Backend.Journal.controller.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String username);

    void DeleteByUserName(String username);
}
