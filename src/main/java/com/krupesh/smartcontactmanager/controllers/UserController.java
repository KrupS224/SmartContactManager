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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
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
    @Autowired
    private MongoTemplate mongoTemplate;

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

            if (!img.isEmpty()) {
                contact.setImage(img.getOriginalFilename());
                File saveImg = new ClassPathResource("static/image").getFile();
                Path imgPath = Paths.get(saveImg.getAbsolutePath() + File.separator + img.getOriginalFilename());
                Files.copy(img.getInputStream(), imgPath, StandardCopyOption.REPLACE_EXISTING);
            }

            this.contactRepository.insert(contact);
            user.getContactIds().add(contact.getContactId());
            this.userRepository.save(user);

            session.setAttribute("message", new Message("Contact added successfully", "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Error: " + e.getMessage(), "alert-danger"));
        }
        return "normal/add_contact_form";
    }

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable Integer page, Model model, Principal principal) {
        model.addAttribute("title", "View Contacts");

        User user = this.userRepository.findUserByEmail(principal.getName());
        Pageable pageable = PageRequest.of(page, 5);
        Page<Contact> contacts = this.contactRepository.findByUserId(user.getId(), pageable);

        if (page > contacts.getTotalPages() - 1) {
            if(contacts.isEmpty()){
                model.addAttribute("noContacts", true);
                return "normal/show_contacts";
            }
            return "redirect:/user/show-contacts/0";
        }

//        System.out.println("Page: " + page + ", totalPages: " + contacts.getTotalPages());
        model.addAttribute("contacts", contacts);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", contacts.getTotalPages() - 1);
        return "normal/show_contacts";
    }

    @GetMapping("/contact/{cID}")
    public String showContactDetails(@PathVariable("cID") ObjectId cID, Model model, Principal principal) {
        Contact contact = this.contactRepository.findByContactId(cID);
        User user = this.userRepository.findUserByEmail(principal.getName());

//        System.out.println("User: " + user.getId() + ", Contact: " + contact.getUserId());
        if (contact == null || !contact.getUserId().toString().equals(user.getId().toString())) {
            return "redirect:/user/show-contacts/0";
        }
        model.addAttribute("title", contact.getName() + "'s Contact Details");
        model.addAttribute("contact", contact);
        return "normal/show_contact_details";
    }

    @PostMapping("/delete-contact/{cID}")
    public String deleteContact(@PathVariable("cID") ObjectId cID, Model model, HttpSession session, Principal principal) {
        Contact contact = this.contactRepository.findByContactId(cID);
        this.contactRepository.delete(contact);

        User user = this.userRepository.findUserByEmail(principal.getName());
        user.getContactIds().remove(cID);
        this.userRepository.save(user);

        session.setAttribute("message", new Message("Contact Deleted Successfully", "alert-success"));
        return "redirect:/user/show-contacts/0";
    }

    @PostMapping("/update-contact/{cID}")
    public String updateForm(@PathVariable("cID") ObjectId cID, Model model) {
        model.addAttribute("title", "Update Contact Form");
        Contact contact = this.contactRepository.findByContactId(cID);
        model.addAttribute("contact", contact);
        return "normal/update_form";
    }

    @PostMapping("/process-update/{cID}")
    public String processUpdate(@PathVariable("cID") ObjectId cID, @ModelAttribute Contact contact, @RequestParam("profileImg") MultipartFile img, HttpSession session) {
        try {
            Contact oldContact = this.contactRepository.findByContactId(cID);
            if(!img.isEmpty()) {
//                Delete Old photo
                if(oldContact.getImage() != null){
                    File oldImg = new ClassPathResource("static/image").getFile();
                    File deleteImg = new File(oldImg, oldContact.getImage());
                    deleteImg.delete();
                }

//                Update New photo
                File saveFile = new ClassPathResource("static/image").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img.getOriginalFilename());
                Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                mongoTemplate.update(Contact.class)
                        .matching(Criteria.where("contactId").is(cID))
                        .apply(new Update().set("image", img.getOriginalFilename()))
                        .first();
            }

            mongoTemplate.update(Contact.class)
                    .matching(Criteria.where("contactId").is(cID))
                    .apply(new Update().set("name", contact.getName())
                            .set("secondName", contact.getSecondName())
                            .set("work", contact.getWork())
                            .set("email", contact.getEmail())
                            .set("phone", contact.getPhone())
                            .set("description", contact.getDescription()))
                            .first();

            session.setAttribute("message", new Message("Contact Updated Successfully", "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/user/contact/{cID}";
    }
}
