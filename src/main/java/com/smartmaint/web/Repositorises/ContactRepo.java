package com.smartmaint.web.Repositorises;

import com.smartmaint.web.Models.Contact;
import com.smartmaint.web.Models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContactRepo extends MongoRepository<Contact, String> {
    List<Contact> findAll(Sort sort);


    long countByReadFalse();

    @Query("{$or: ["
            + "{'firstName': {$regex: ?0, $options: 'i'}},"
            + "{'lastName': {$regex: ?1, $options: 'i'}},"
            + "{'subject': {$regex: ?2, $options: 'i'}},"
            + "{'email': {$regex: ?3, $options: 'i'}},"
            + "{'phoneNumber': {$regex: ?4, $options: 'i'}},"
            + "{'message': {$regex: ?5, $options: 'i'}}"
            + "]}")
    List<Contact> searchContacts(String firstName, String lastName, String subject, String email, String phoneNumber, String message, Sort sort);


}
