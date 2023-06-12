package com.smartmaint.web.Controllers;

import com.smartmaint.web.Auth.AuthenticationResponse;
import com.smartmaint.web.Auth.AuthenticationService;
import com.smartmaint.web.Models.User;
import com.smartmaint.web.Services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
public class RegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService service;
    @RequestMapping("/register")
    public String displayRegisterPage(Model model,
                                      @RequestParam(value = "typeNonValid", required = false) boolean typeNonValid,
                                      @RequestParam(value = "cvSize", required = false) boolean cvSize){

        model.addAttribute("typeNonValid", typeNonValid);
        model.addAttribute("cvSize", cvSize);
        model.addAttribute("user", new User());
        return "register.html";
    }

    @RequestMapping("/adminRegistrations")
    public String displayAdminRegistrationsPage(Model model, HttpSession session){
        model.addAttribute("adminUser", new User());

        Object sentAttribute = session.getAttribute("registerSuccess");
        if (sentAttribute != null && (boolean) sentAttribute) {
            model.addAttribute("registerSuccess", true);
            session.removeAttribute("registerSuccess"); // Remove the attribute from the session
        } else {
            model.addAttribute("registerSuccess", false);
        }

        return "adminRegistrations.html";
    }

    @PostMapping("/admin/Registrations")
    public String adminRegistration(Model model, @Valid @ModelAttribute("adminUser") User user, Errors errors, HttpSession session){
        if (errors.hasErrors()){
            return "/adminRegistrations";
        }

        if (userService.emailExists(user.getEmail().toLowerCase())){
            model.addAttribute("checkEmail", true);
            log.info("email already exists");
            return "/adminRegistrations";
        }

        if (user.getPassword().equals(user.getCheckPass()) != true){
            model.addAttribute("checkPassResult", true);
            log.info("Confirmation password does not match !");
            return "/adminRegistrations";
        }

        AuthenticationResponse JWTToken = service.register(user);
        session.setAttribute("registerSuccess", true);
        return "redirect:/adminRegistrations";
    }






}
