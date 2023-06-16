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


    //@ModelAttribute("updateAdmin") User user
    @PostMapping("/admin/changePassword")
    public String changePassAdmin(Model model, HttpSession httpSession, @ModelAttribute("updateAdmin") User user){
        if(user.getPassword().isEmpty()){
            model.addAttribute("checkPassResultempty", true);
            return "adminProfile";
        }

        if(!user.getPassword().equals(user.getCheckPass())){
            model.addAttribute("checkPassResult", true);
            return "adminProfile";
        }

        String token = (String) httpSession.getAttribute("JwtToken");
        if (token != null && !token.isEmpty()) {
            String email = (String) jwtHelper.extractUsername(token);
            log.info("email >>> {}", email);

            var user_ = userRepo.findByEmail(email.toLowerCase())
                    .orElseThrow(() -> new IllegalStateException("Email recover does not exist!"));

            if (!passwordEncoder.matches(user.getOldPassword(), user_.getPassword())) {
                model.addAttribute("checkOldPass", true);
                return "adminProfile";
            }
            userService.changePassword(email, user.getPassword());
            model.addAttribute("passChanged", true);
            return "adminProfile";
        }
        return "adminProfile";
    }

}
