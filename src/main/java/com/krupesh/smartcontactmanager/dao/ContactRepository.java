package com.krupesh.smartcontactmanager.dao;

import com.krupesh.smartcontactmanager.entities.Contact;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<Contact, ObjectId> {
}
