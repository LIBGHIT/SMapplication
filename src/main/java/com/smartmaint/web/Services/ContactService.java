package com.smartmaint.web.Services;

import com.smartmaint.web.Models.Contact;
import com.smartmaint.web.Repositorises.ContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactRepo contactRepository;

    public List<Contact> getAllContacts(Sort sort) {
        return contactRepository.findAll(sort);
    }

    public void markContactAsRead(String id) {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact != null) {
            contact.setRead(true);
            contactRepository.save(contact);
        }
    }


    public long countUnreadMessages() {
        return contactRepository.countByReadFalse();
    }


    public Contact getContactById(String id) {
        return contactRepository.findById(id).orElse(null);
    }


    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }


    public void deleteContact(String id) {
        contactRepository.deleteById(id);
    }


    public List<Contact> searchContacts(String search, String sortDirection) {
        String searchTerm = ".*" + search + ".*";
        Sort sort = Sort.by("dateSent").ascending();

        if ("newest".equalsIgnoreCase(sortDirection)) {
            sort = Sort.by("dateSent").descending();
        }

        return contactRepository.searchContacts(searchTerm, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm, sort);
    }

}