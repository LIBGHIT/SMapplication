package com.smartmaint.web.Repositorises;

import com.smartmaint.web.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Page<User> findByRole(String role, Pageable pageable);
}
