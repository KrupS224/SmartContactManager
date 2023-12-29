package com.krupesh.smartcontactmanager.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "contacts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    private ObjectId contactId;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 30, message = "Name size should be between 2 to 30 characters")
    private String name;

    @Size(max = 30, message = "Second name size should not exceed 30 characters")
    private String secondName;

    @Size(max = 100, message = "Work size should not exceed 100 characters")
    private String work;

    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 15, message = "Phone number should not exceed 15 characters")
    private String phone;

    @Size(max = 255, message = "Image URL length should not exceed 255 characters")
    private String image;

    @Length(max = 1000, message = "Description length should not exceed 1000 characters")
    private String description;

    @JsonBackReference
    private ObjectId userId;
}
