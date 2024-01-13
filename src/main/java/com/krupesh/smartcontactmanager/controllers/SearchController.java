package com.krupesh.smartcontactmanager.controllers;

import com.krupesh.smartcontactmanager.dao.ContactRepository;
import com.krupesh.smartcontactmanager.dao.UserRepository;
import com.krupesh.smartcontactmanager.entities.Contact;
import com.krupesh.smartcontactmanager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal, Model model) {
//        System.out.println(query);
        User user = this.userRepository.findUserByEmail(principal.getName());
        List<Contact> contacts = this.contactRepository.findByNameContainingAndUserId(query, user.getId());
//        System.out.println(contacts);
        return ResponseEntity.ok(contacts);
    }
}
