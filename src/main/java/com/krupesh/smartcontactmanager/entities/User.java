package com.krupesh.smartcontactmanager.entities;

import jakarta.validation.constraints.*;

import lombok.*;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;

    @NotBlank(message = "Name cannot be blank !!")
    @Size(min = 2, max = 30, message = "Name size should be between 2 to 30 characters")
    private String name;

    @Indexed(unique = true)
    @NotBlank(message = "Email cannot be blank !!")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank !!")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String roll;

    private boolean enabled;

    @Length(max = 255, message = "Image URL length should not exceed 255 characters")
    private String imgUrl;

    @Length(max = 500, message = "About length should not exceed 500 characters")
    private String about;

    @DBRef
    @Field(targetType = FieldType.OBJECT_ID)
    private List<Contact> contacts = new ArrayList<>();
}
