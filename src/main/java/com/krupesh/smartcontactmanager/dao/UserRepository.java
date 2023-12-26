package com.krupesh.smartcontactmanager.dao;

import com.krupesh.smartcontactmanager.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    public User findUserByEmail(String Email);
}
