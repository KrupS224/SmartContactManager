package com.krupesh.smartcontactmanager.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.krupesh.smartcontactmanager.entities.Contact;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ContactDeserializer extends JsonDeserializer<Contact> {
    @Override
    public Contact deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String contactId = node.get("contactId").asText();
        // Deserialize other properties as needed
        return new Contact(new ObjectId(contactId));
    }
}