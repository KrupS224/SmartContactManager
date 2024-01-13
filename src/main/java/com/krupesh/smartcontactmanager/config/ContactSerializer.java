package com.krupesh.smartcontactmanager.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.krupesh.smartcontactmanager.entities.Contact;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ContactSerializer extends JsonSerializer<Contact> {
    @Override
    public void serialize(Contact contact, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("contactId", contact.getContactId().toString());
        // Serialize other properties as needed
        jsonGenerator.writeEndObject();
    }
}

