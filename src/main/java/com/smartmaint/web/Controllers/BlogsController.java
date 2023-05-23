package com.smartmaint.web.Controllers;

import com.smartmaint.web.Models.Blogs;
import com.smartmaint.web.Models.Comment;
import com.smartmaint.web.Services.BlogService;
import com.smartmaint.web.Services.CommentService;
import com.smartmaint.web.Services.FileService;
import com.smartmaint.web.jwt.JwtHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
public class BlogsController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    private FileService fileService;

    @RequestMapping("/detailblog")
    public String displayDetailBlogPage(){
        return "detailblog.html";
    }


    @RequestMapping("/blogposts")
    public String displayBlogPostsPage(){

        return "blogposts.html";
    }

    @RequestMapping("/blogmanage")
    public String displayBlogManagePage(){

        return "blogmanage.html";
    }

    @RequestMapping("/blogedit")
    public String displayBlogEditPage(){

        return "blogedit.html";
    }

    @RequestMapping(value = "/blogadd")
    public String displayBlogAddPage(Model model){
        model.addAttribute("blog", new Blogs());
        return "blogadd.html";
    }



    @PostMapping("/addPost")
    public String addBlog(@ModelAttribute("blog") @Valid Blogs blog, Errors errors, @RequestParam("image") MultipartFile image) throws IOException {
        if (errors.hasErrors()) {
            return "blogadd.html";
        }


        blog.setDate(new Date());
        blogService.addBlog(blog,image);
        return "redirect:/blogmanage";
    }

    @GetMapping("/blogposts")
    public String getAllBlogs(Model model, @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "3") int size) {
        Page<Blogs> blogsPage = blogService.getAllNonArchivedBlogs(page, size);
        model.addAttribute("blogs", blogsPage.getContent());
        model.addAttribute("totalPages", blogsPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "blogposts";
    }



    @GetMapping("/blogmanage")
    public String getAllBlogsTabl(Model model, @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size) {
        Page<Blogs> blogs = blogService.getAllBlogsPageable(page, size);
        model.addAttribute("blogs", blogs.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogs.getTotalPages());
        model.addAttribute("totalBlogs", blogService.countBlogs());
        model.addAttribute("totalActiveBlogs", blogService.countActiveBlogs()); // Ajout de cette ligne
        return "blogmanage";
    }
    @GetMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
        return "redirect:/blogmanage";
    }


    @GetMapping("/blogedit/{id}")
    public String editBlog(Model model, @PathVariable String id) {
        Blogs blog = blogService.getBlogById(id); // Récupérer le blog à partir de la base de données
        model.addAttribute("blog", blog); // Ajouter le blog au modèle
        return "blogedit"; // Afficher la page d'édition du blog
    }

    @PostMapping("/updatePost")
    public String updateBlog(@ModelAttribute("blog") @Valid Blogs updatedBlog, Errors errors, @RequestParam("image") MultipartFile image) throws IOException {
        if (errors.hasErrors()) {
            return "blogedit";
        }

        Blogs existingBlog = blogService.getBlogById(updatedBlog.getId_blog()); // Récupérer le blog existant de la base de données

        existingBlog.setTitle(updatedBlog.getTitle()); // Mettre à jour le titre du blog
        existingBlog.setSubject(updatedBlog.getSubject()); // Mettre à jour le sujet du blog
        existingBlog.setExcerpt(updatedBlog.getExcerpt()); // Mettre à jour l'extrait du blog
        existingBlog.setBody(updatedBlog.getBody()); // Mettre à jour le corps du blog

        if (image != null && !image.isEmpty()) {
            // Mettre à jour l'image si une nouvelle image est fournie
            String imageId = fileService.addFile(image);
            existingBlog.setImageId(imageId);
        }

        blogService.updateBlog(existingBlog); // Mettre à jour le blog dans la base de données

        return "redirect:/blogmanage"; // Rediriger vers la page de gestion de blog
    }

    @GetMapping("/toggleArchiveBlog/{id}")
    public String toggleArchiveBlog(@PathVariable String id) {
        Blogs blog = blogService.getBlogById(id);
        blog.setArchived(!blog.isArchived());  // inverse l'état d'archivage
        blogService.updateBlog(blog);
        return "redirect:/blogmanage";
    }






    @GetMapping("/blogs")
    public String getAllBlogsForNewPage(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "4") Integer size,
            Model model,
            HttpSession httpSession) {

        String jwtToken = (String) httpSession.getAttribute("JwtToken");

        if (jwtToken == null || jwtToken.isEmpty()) {
            String checkPoint = "redirect:/blogs";
            httpSession.setAttribute("checkPoint", checkPoint);
        }

        Page<Blogs> blogs = blogService.getAllNonArchivedBlogs(page, size);
        List<Map> subjects = blogService.countBlogsBySubject();

        model.addAttribute("subjects", subjects);
        model.addAttribute("blogs", blogs.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogs.getTotalPages());
        model.addAttribute("size", size);

        return "blogs";
    }




    @GetMapping("/detailblog/{id}")
    public String showBlogDetails(@PathVariable("id") String id, Model model, HttpSession httpSession) {
        String token = (String) httpSession.getAttribute("JwtToken");

        // Vérifier si le token est présent
        if (token != null && !token.isEmpty()) {
            // Extraire les informations du nom et du prénom à partir du token JWT
            String firstName = (String) jwtHelper.extractClaim(token, claims -> claims.get("firstName"));
            String lastName = (String) jwtHelper.extractClaim(token, claims -> claims.get("lastName"));

            // Ajouter les informations au modèle pour les utiliser dans la vue
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
        }
        Blogs blog = blogService.getBlogById(id); // Obtenez le blog en utilisant le service.
        if (blog != null) {
            model.addAttribute("blog", blog);
            List<Map> subjects = blogService.countBlogsBySubject();
            model.addAttribute("subjects", subjects);// Ajoutez le blog au modèle.
            List<Comment> comments = commentService.getCommentsByBlogId(id);
            if (comments == null) {
                comments = new ArrayList<>();
            }
            Collections.sort(comments, Comparator.comparing(Comment::getDate).reversed());

            model.addAttribute("comment", comments);
            return "detailblog"; // Retournez la vue 'detailblog'.
        } else {
            return "redirect:/"; // Redirigez vers la page d'accueil si le blog n'est pas trouvé.
        }
    }




}




