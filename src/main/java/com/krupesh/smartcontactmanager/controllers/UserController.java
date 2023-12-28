package com.krupesh.smartcontactmanager.controllers;

import com.krupesh.smartcontactmanager.dao.ContactRepository;
import com.krupesh.smartcontactmanager.dao.UserRepository;
import com.krupesh.smartcontactmanager.entities.Contact;
import com.krupesh.smartcontactmanager.entities.User;
import com.krupesh.smartcontactmanager.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

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
            contact.setUser(user);

            if(!img.isEmpty()) {
                contact.setImage(img.getOriginalFilename());
                File saveImg = new ClassPathResource("static/image").getFile();
                Path imgPath = Paths.get(saveImg.getAbsolutePath() + File.separator + img.getOriginalFilename());
                Files.copy(img.getInputStream(), imgPath, StandardCopyOption.REPLACE_EXISTING);
            }

            this.contactRepository.insert(contact);
            user.getContacts().add(contact);
            this.userRepository.save(user);

            session.setAttribute("message", new Message("Contact added successfully", "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Error: " + e.getMessage(), "alert-danger"));
        }
        return "normal/add_contact_form";
    }

}
