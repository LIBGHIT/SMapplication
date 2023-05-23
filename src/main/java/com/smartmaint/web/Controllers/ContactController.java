package com.smartmaint.web.Controllers;

import com.smartmaint.web.Models.Contact;
import com.smartmaint.web.Services.ContactService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping("/messages/{sort}/{id}")
    public String displayMessagesPage(@RequestParam(required = false) String search,
                                      @RequestParam(required = false) String delId,
                                      @PathVariable(required = false) String sort,
                                      @PathVariable(required = false) String id,
                                      Model model){
        if(delId != null){
            contactService.deleteContact(delId);
        }

        if (sort != null){
            model.addAttribute("sort", sort);
        }
        if (search != null){
            model.addAttribute("searchTerm", search);
        }

        if (id != null) {
            contactService.markContactAsRead(id);
            Contact contact = contactService.getContactById(id);
            if (contact != null) {
                model.addAttribute("contact", contact);
                model.addAttribute("id", id);
            }
        }
        return "messages";
    }

    @ModelAttribute("contacts")
    public List<Contact> getAllContacts(@RequestParam(required = false) String search,
                                        @PathVariable(required = false) String sort,
                                        Model model) {

        if(sort != null){
            if (!sort.equals("newest") && !sort.equals("oldest")) {
                // If not, set it to a default value
                sort = "newest";
            }
        }else {
            sort = "newest";
        }

        if (search != null){
            log.info("this is the search value >>>> {}", sort);
            return contactService.searchContacts(search, sort);
        }

        Sort sortOrder = sort.equals("newest") ? Sort.by("dateSent").descending() : Sort.by("dateSent").ascending();
        model.addAttribute("sort", sort);
        return contactService.getAllContacts(sortOrder);
    }


    @PostMapping("/sendMessage")
    public String sendMessage(@Valid @ModelAttribute("contact") Contact contact, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            return "contact";
        }
        contact.setDateSent(new Date());
        contact.setRead(false);
        contactService.saveContact(contact);
        session.setAttribute("sent", true);
        return "redirect:/contact";
    }

    @GetMapping("/contact")
    public String displayContactPage(Model model, HttpSession session) {
        model.addAttribute("contact", new Contact());

        Object sentAttribute = session.getAttribute("sent");
        if (sentAttribute != null && (boolean) sentAttribute) {
            model.addAttribute("sent", true);
            session.removeAttribute("sent"); // Remove the attribute from the session
        } else {
            model.addAttribute("sent", false);
        }

        return "contact.html";
    }








    @GetMapping("/messages")
    public String displayMessagesPage(Model model){
        return "messages";
    }

}


