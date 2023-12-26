package com.krupesh.smartcontactmanager.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "contacts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    private ObjectId cID;
    private String name;
    private String secondName;
    private String work;
    private String email;
    private String phone;
    private String image;
    @Length(max = 1000)
    private String description;

    @DBRef
    private User user;

}
