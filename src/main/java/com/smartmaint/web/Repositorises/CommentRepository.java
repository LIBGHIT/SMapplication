package com.smartmaint.web.Repositorises;

import com.smartmaint.web.Models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByBlogId(String blogId);
}

