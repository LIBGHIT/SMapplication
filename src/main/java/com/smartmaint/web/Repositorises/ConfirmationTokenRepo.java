package com.smartmaint.web.Repositorises;

import com.smartmaint.web.Confirmation.ConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationTokenRepo extends MongoRepository<ConfirmationToken,String> {
    Optional<ConfirmationToken> findByToken(String token);

    void deleteByEmail(String email);

}
