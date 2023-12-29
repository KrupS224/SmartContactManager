package com.krupesh.smartcontactmanager.dao;

import com.krupesh.smartcontactmanager.entities.Contact;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<Contact, ObjectId> {
    Page<Contact> findByUserId(ObjectId userId, Pageable pageable);
    Contact findByContactId(ObjectId cID);
}
