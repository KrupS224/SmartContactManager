package com.krupesh.smartcontactmanager.controllers;

import com.krupesh.smartcontactmanager.dao.UserRepository;
import com.krupesh.smartcontactmanager.entities.User;
import com.krupesh.smartcontactmanager.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")  // Add this annotation to specify the mapping
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("title", "Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About US");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register Here");
        model.addAttribute("user", new User());
        return "signup";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login Here");
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult errorResult, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session) {
        try {
            if(errorResult.hasErrors()) {
                System.out.println("Error: " + errorResult.toString());
                model.addAttribute("user", user);
                return "signup";
            }

            if(!agreement) {
                throw new Exception("You have not agreed terms and conditions");
            }

            user.setEnabled(true);
            user.setRoll("USER");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            try {
                this.userRepository.insert(user);
            } catch (Exception e) {
                e.printStackTrace();
            }

            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully Registered", "alert-success"));

            return "home";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Error: " + e.getMessage(), "alert-danger"));

            return "signup";
        }
    }

}
