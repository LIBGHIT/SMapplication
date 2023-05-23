package com.smartmaint.web.Repositorises;

import com.smartmaint.web.Models.Blogs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BlogRepo extends MongoRepository<Blogs, String> {

    Optional<Blogs> findById(String id);
    Page<Blogs> findByArchivedFalse(Pageable pageable);
    long countByArchivedFalse();


}
