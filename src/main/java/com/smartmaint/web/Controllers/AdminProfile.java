package com.smartmaint.web.Controllers;

import com.smartmaint.web.Models.User;
import com.smartmaint.web.Repositorises.UserRepo;
import com.smartmaint.web.Services.UserService;
import com.smartmaint.web.jwt.JwtHelper;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class AdminProfile {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/adminProfile")
    public String displayAdminProfileUpdate(Model model){
        model.addAttribute("updateAdmin", new User());
        return "adminProfilePassword";
    }

    @PostMapping("/admin/changePassword")
    public ModelAndView changePassAdmin(HttpSession httpSession, @ModelAttribute("updateAdmin") User user){
        ModelAndView modelAndView = new ModelAndView("adminProfilePassword");

        if(user.getPassword().isEmpty()){
            modelAndView.addObject("checkPassResultempty", true);
            return modelAndView;
        }

        if(!user.getPassword().equals(user.getCheckPass())){
            modelAndView.addObject("checkPassResult", true);
            return modelAndView;
        }

        String token = (String) httpSession.getAttribute("JwtToken");
        if (token != null && !token.isEmpty()) {
            String email = (String) jwtHelper.extractUsername(token);
            log.info("email >>> {}", email);

            var user_ = userRepo.findByEmail(email.toLowerCase())
                    .orElseThrow(() -> new IllegalStateException("Email recover does not exist!"));

            if (!passwordEncoder.matches(user.getOldPassword(), user_.getPassword())) {
                modelAndView.addObject("checkOldPass", true);
                return modelAndView;
            }
            userService.changePassword(email, user.getPassword());
            modelAndView.addObject("passChanged", true);
            return modelAndView;
        }
        return modelAndView;
    }


}
