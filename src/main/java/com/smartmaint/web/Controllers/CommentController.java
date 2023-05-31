package com.smartmaint.web.Controllers;

import com.smartmaint.web.Models.Comment;
import com.smartmaint.web.Services.CommentService;
import com.smartmaint.web.jwt.JwtHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartmaint.web.Services.CommentService;
import java.util.Date;
import java.security.Principal;


@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable String id) {
        Comment comment = commentService.getCommentById(id);
        return comment != null ? ResponseEntity.ok(comment) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Comment saveComment(@RequestBody Comment comment) {
        return commentService.saveComment(comment);
    }

    @GetMapping("/blog/{blogId}")
    public List<Comment> getCommentsByBlogId(@PathVariable String blogId) {
        return commentService.getCommentsByBlogId(blogId);
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam String text, @RequestParam String blogId,
                             @RequestParam(required = false) String parentCommentId,
                             Model model, HttpSession httpSession){
        String token = (String) httpSession.getAttribute("JwtToken");

        String firstName = "";
        String lastName  = "";
        String userId    = "";
        String role      = "";

        if (token != null && !token.isEmpty()) {

             firstName =    (String) jwtHelper.extractClaim(token, claims -> claims.get("firstName"));
             lastName  =     (String) jwtHelper.extractClaim(token, claims -> claims.get("lastName"));
             userId    =       (String) jwtHelper.extractClaim(token, claims -> claims.get("userId"));
             role      =         (String) jwtHelper.extractClaim(token, claims -> claims.get("role"));

            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName" , lastName);
            model.addAttribute("userId"   , userId);
            model.addAttribute("role"     , role);

        }

        Comment comment = new Comment();
        comment.setText(text);
        comment.setBlogId(blogId);
        comment.setDate(new Date());
        comment.setFirstName(firstName);
        comment.setLastName(lastName);
        comment.setUserId(userId);
        comment.setRole(role);

        commentService.saveComment(comment);
        return "redirect:/detailblog/" + blogId;
    }

    @GetMapping("/deleteComment/{id}")
    public String deleteComment(@PathVariable String id,@RequestParam String blogId) {
        commentService.deleteComment(id);
        return "redirect:/detailblog/" + blogId;
    }

}
