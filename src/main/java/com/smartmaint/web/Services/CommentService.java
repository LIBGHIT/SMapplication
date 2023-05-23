package com.smartmaint.web.Services;

import com.smartmaint.web.Models.Comment;
import com.smartmaint.web.Repositorises.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment getCommentById(String id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByBlogId(String blogId) {
        return commentRepository.findByBlogId(blogId);
    }

    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }
}
