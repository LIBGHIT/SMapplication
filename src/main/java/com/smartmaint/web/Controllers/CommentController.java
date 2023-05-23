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
    public String addComment(@RequestParam String text, @RequestParam String blogId,@RequestParam(required = false) String parentCommentId, Model model, HttpSession httpSession) {
        String token = (String) httpSession.getAttribute("JwtToken");

        String firstName = ""; // Déclaration en dehors de la condition avec une valeur par défaut
        String lastName = ""; // Déclaration en dehors de la condition avec une valeur par défaut
        String userId = "";

        // Vérifier si le token est présent
        if (token != null && !token.isEmpty()) {
            // Extraire les informations du nom et du prénom à partir du token JWT
             firstName = (String) jwtHelper.extractClaim(token, claims -> claims.get("firstName"));
             lastName = (String) jwtHelper.extractClaim(token, claims -> claims.get("lastName"));
            userId = (String) jwtHelper.extractClaim(token, claims -> claims.get("Id_User"));



            // Ajouter les informations au modèle pour les utiliser dans la vue
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            // Ajouter l'ID de l'utilisateur au modèle pour l'utiliser dans la vue
            model.addAttribute("userId", userId);

        }

        Comment comment = new Comment();
        comment.setText(text);
        comment.setBlogId(blogId);
        comment.setDate(new Date()); //ajoutez la date actuelle au commentaire
        comment.setFirstName(firstName);
        comment.setLastName(lastName);
        comment.setUserId(userId);
//        if (parentCommentId != null && !parentCommentId.isEmpty()) {
//            comment.setParentCommentId(parentCommentId); // Définir l'ID du commentaire parent si présent
//        }

        commentService.saveComment(comment); //sauvegardez le commentaire dans la base de données
        // Ajouter d'autres actions nécessaires ici, comme la redirection vers la page du blog
        return "redirect:/detailblog/" + blogId; // redirige vers la page du blog après l'ajout d'un commentaire
    }



    @GetMapping("/deleteBlog/{id}")
    public String deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return "redirect:/detailblog";
    }


}
