package com.krupesh.smartcontactmanager.controllers;

import com.krupesh.smartcontactmanager.dao.ContactRepository;
import com.krupesh.smartcontactmanager.dao.UserRepository;
import com.krupesh.smartcontactmanager.entities.Contact;
import com.krupesh.smartcontactmanager.entities.User;
import com.krupesh.smartcontactmanager.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("title", "View Your Profile");
        return "normal/user_profile";
    }

    @GetMapping("/add-contact")
    public String addContact(Model model) {
        model.addAttribute("title", "Add Contacts");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImg") MultipartFile img, Principal principal, HttpSession session) {
        try {
            User user = this.userRepository.findUserByEmail(principal.getName());
            contact.setUserId(user.getId());

            if(!img.isEmpty()) {
                File saveImg = new ClassPathResource("static/image").getFile();
                Path imgPath = Paths.get(saveImg.getAbsolutePath() + File.separator + img.getOriginalFilename());
                contact.setImage(String.valueOf(imgPath));
                Files.copy(img.getInputStream(), imgPath, StandardCopyOption.REPLACE_EXISTING);
            }

            this.contactRepository.insert(contact);
            user.getContactIds().add(contact.getCID());
            this.userRepository.save(user);

            session.setAttribute("message", new Message("Contact added successfully", "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Error: " + e.getMessage(), "alert-danger"));
        }
        return "normal/add_contact_form";
    }

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable Integer page, Model model, Principal principal){
        model.addAttribute("title", "View Contacts");

        User user = this.userRepository.findUserByEmail(principal.getName());
        Pageable pageable = PageRequest.of(page, 5);
        Page<Contact> contacts = this.contactRepository.findByUserId(user.getId(), pageable);
        contacts.forEach(contact -> {
            if(contact.getImage() == null) {
                contact.setImage("https://www.seekpng.com/png/detail/966-9665493_my-profile-icon-blank-profile-image-circle.png");
            }
        });

        model.addAttribute("contacts", contacts);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", contacts.getTotalPages());
        return "normal/show_contacts";
    }

}
