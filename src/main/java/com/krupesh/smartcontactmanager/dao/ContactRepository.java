package com.krupesh.smartcontactmanager.dao;

import com.krupesh.smartcontactmanager.entities.Contact;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ContactRepository extends MongoRepository<Contact, ObjectId> {
//    JSON Query
    List<Contact> findByUserId(ObjectId userId);
}
