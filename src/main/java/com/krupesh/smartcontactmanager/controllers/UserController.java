package com.krupesh.smartcontactmanager.controllers;

import com.krupesh.smartcontactmanager.dao.UserRepository;
import com.krupesh.smartcontactmanager.entities.Contact;
import com.krupesh.smartcontactmanager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

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

}
