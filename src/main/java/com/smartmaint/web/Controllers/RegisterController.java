package com.smartmaint.web.Controllers;

import com.smartmaint.web.Auth.AuthenticationResponse;
import com.smartmaint.web.Auth.AuthenticationService;
import com.smartmaint.web.Models.Skill;
import com.smartmaint.web.Models.User;
import com.smartmaint.web.Services.SkillService;
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
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class RegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService service;
    @Autowired
    private SkillService skillService;
    @RequestMapping("/register")
    public String displayRegisterPage(Model model,
                                      @RequestParam(value = "typeNonValid", required = false) boolean typeNonValid,
                                      @RequestParam(value = "cvSize", required = false) boolean cvSize){
        List<Skill> skills = skillService.getAllSkills();
        model.addAttribute("skills", skills);
        model.addAttribute("typeNonValid", typeNonValid);
        model.addAttribute("cvSize", cvSize);
        model.addAttribute("user", new User());
        return "register";
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


    //SKILLS

    @RequestMapping("/RegistrationsSettings")
    public String displayRegistrationSettings(Model model) {
        List<Skill> skills = skillService.getAllSkills();
        model.addAttribute("skills", skills);
        model.addAttribute("skill", new Skill());
        return "registrationsettings";
    }


    @PostMapping("/admin/addSkill")
    public ModelAndView addNewSkill(@ModelAttribute("skill") Skill skill, Model model){
        ModelAndView modelAndView = new ModelAndView("registrationsettings");
        modelAndView.addObject("skill", new Skill());

        skillService.addSkill(skill);

        List<Skill> skills = skillService.getAllSkills();
        modelAndView.addObject("skills", skills);

        modelAndView.addObject("skillAdded", true);
        return modelAndView;
    }

    @PostMapping("/admin/deleteSkill")
    public ModelAndView deleteSkill(@RequestParam("selectedSkill") String selectedSkillId) {
        ModelAndView modelAndView = new ModelAndView("registrationsettings");
        modelAndView.addObject("skill", new Skill());

        skillService.deleteSkillById(selectedSkillId);

        List<Skill> skills = skillService.getAllSkills();
        modelAndView.addObject("skills", skills);

        modelAndView.addObject("skillDeleted", true);
        return modelAndView;
    }

}
