package com.krupesh.smartcontactmanager.dao;

import com.krupesh.smartcontactmanager.entities.Contact;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContactRepository extends MongoRepository<Contact, ObjectId> {
    Page<Contact> findByUserId(ObjectId userId, Pageable pageable);
}
