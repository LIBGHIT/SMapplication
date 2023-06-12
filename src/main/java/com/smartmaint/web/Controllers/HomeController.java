package com.smartmaint.web.Controllers;

import com.smartmaint.web.Models.Contact;
import com.smartmaint.web.jwt.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    JwtHelper jwtHelper;

    @RequestMapping("home")
    public String displayHomePage(HttpSession httpSession, Model model){
        String token = (String) httpSession.getAttribute("JwtToken");
        if (token != null && !token.isEmpty()) {
            String firstName = (String) jwtHelper.extractClaim(token, claims -> claims.get("firstName"));
            String lastName = (String) jwtHelper.extractClaim(token, claims -> claims.get("lastName"));
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
        }
        return "home.html";
    }

    @RequestMapping(value = {"", "/"})
    public String displayHomePages(HttpSession httpSession, Model model){
        return "redirect:/home";
    }



}

