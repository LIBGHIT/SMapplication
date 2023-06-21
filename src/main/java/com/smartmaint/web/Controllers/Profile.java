package com.smartmaint.web.Controllers;

import com.smartmaint.web.Models.Skill;
import com.smartmaint.web.Models.User;
import com.smartmaint.web.Repositorises.UserRepo;
import com.smartmaint.web.Services.SkillService;
import com.smartmaint.web.Services.UserService;
import com.smartmaint.web.jwt.JwtHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
public class Profile {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SkillService skillService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @RequestMapping("/profile")
    public String displayProfilePage(Model model, HttpSession httpSession ){
        model.addAttribute("user", new User());

        //get all skills
        List<Skill> skills = skillService.getAllSkills();
        model.addAttribute("skills", skills);

        String token = (String) httpSession.getAttribute("JwtToken");
        if (token != null && !token.isEmpty()) {
            String email = (String) jwtHelper.extractUsername(token);
            log.info("email >>> {}", email);

            var user_ = userRepo.findByEmail(email.toLowerCase())
                    .orElseThrow(() -> new IllegalStateException("profile jwt token null!"));
            model.addAttribute("currentUser", user_);
        }

        return "profiles";
    }

    @RequestMapping("/update")
    public ModelAndView updateUser(HttpSession httpSession,@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView("profiles");
        List<Skill> skills = skillService.getAllSkills();
        modelAndView.addObject("skills", skills);

        String token = (String) httpSession.getAttribute("JwtToken");
        if (token != null && !token.isEmpty()) {
            String email = (String) jwtHelper.extractUsername(token);
            var existingUser = userRepo.findByEmail(email.toLowerCase())
                    .orElseThrow(() -> new IllegalStateException("profile jwt token null!"));

            if (!passwordEncoder.matches(user.getOldPassword(), existingUser.getPassword())) {
                modelAndView.addObject("checkOldPass", true);
                modelAndView.addObject("currentUser", existingUser);
                modelAndView.addObject("skills", skills);
                return modelAndView;
            }

            if (user.getPassword().isEmpty()) {
                modelAndView.addObject("emptyPass", true);
                modelAndView.addObject("currentUser", existingUser);
                modelAndView.addObject("skills", skills);
                return modelAndView;
            }

            // Update the relevant fields with the new information
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            existingUser.setSector(user.getSector());
            existingUser.setSkills(user.getSkills());

            // Save the updated user in the database
            userRepo.save(existingUser);

            modelAndView.addObject("updated", true);
            modelAndView.addObject("currentUser", existingUser);
            modelAndView.addObject("skills", skills);
            log.info("user >>>> {}", existingUser);
        }

        return modelAndView;
    }
}
