package com.smartmaint.web.Controllers;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.smartmaint.web.Models.Blogs;
import com.smartmaint.web.Models.User;
import com.smartmaint.web.Services.BlogService;
import com.smartmaint.web.Services.FileService;
import com.smartmaint.web.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class  UsersController {

    @Autowired
    private UserService userservice;
    @Autowired
    private FileService fileService;
    @RequestMapping("/users")
    public String displayUsersPage(){ return "users.html";
    }

    @GetMapping("/users")
    public String getUsers(Model model, @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "5") int size) {
        Page<User> usersPage = userservice.getUsersPageable(page, size);
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("totalUsers", usersPage.getTotalElements());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("currentPage", page);

        Page<User> adminsPage = userservice.getAdminUsersPageable(page, size);
        model.addAttribute("admins", adminsPage.getContent());
        model.addAttribute("totalAdmins", adminsPage.getTotalElements());
        model.addAttribute("totalPageAdmins", adminsPage.getTotalPages());
        model.addAttribute("currentPageAdmin", page);

        Page<User> smusersPage = userservice.getSmUsersPageable(page, size);
        model.addAttribute("smusers", smusersPage.getContent());
        model.addAttribute("totalSmUsers", smusersPage.getTotalElements());
        model.addAttribute("totalSmPages", smusersPage.getTotalPages());
        model.addAttribute("currentSmPage", page);


        return "users";
    }



    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable String id) {
        userservice.deleteUser(id);
        return "redirect:/users";
    }
}
