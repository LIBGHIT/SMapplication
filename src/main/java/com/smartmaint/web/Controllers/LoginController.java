package com.smartmaint.web.Controllers;

import com.smartmaint.web.Auth.AuthenticationRequest;
import com.smartmaint.web.Auth.AuthenticationService;
import com.smartmaint.web.Models.User;
import com.smartmaint.web.Services.ConfirmationTokenService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class LoginController {
    @Autowired
    private AuthenticationService authenticationService;


    @RequestMapping("/login")
    public String displayRegisterPage(Model model,
                                      @RequestParam(value = "alreadyConfirmed", required = false) boolean alreadyConfirmed,
                                      @RequestParam(value = "tokenExpired", required = false) boolean tokenExpired,
                                      @RequestParam(value = "emailOrPassIncorrect", required = false) boolean emailOrPassIncorrect,
                                      @RequestParam(value = "emailNotValidated", required = false) boolean emailNotValidated,
                                      @RequestParam(value = "validationSuccess", required = false) boolean validationSuccess,
                                      @RequestParam(value = "passwordChanged", required = false) boolean passwordChanged,
                                      @RequestParam(value = "passTokenExpired", required = false) boolean passTokenExpired,
                                      @RequestParam(value = "passTokenUsed", required = false) boolean passTokenUsed){
        model.addAttribute("passTokenUsed", passTokenUsed);
        model.addAttribute("passTokenExpired", passTokenExpired);
        model.addAttribute("loginRequest", new AuthenticationRequest());
        model.addAttribute("alreadyConfirmed", alreadyConfirmed);
        model.addAttribute("tokenExpired", tokenExpired);
        model.addAttribute("emailOrPassIncorrect", emailOrPassIncorrect);
        model.addAttribute("emailNotValidated", emailNotValidated);
        model.addAttribute("validationSuccess", validationSuccess);
        model.addAttribute("passwordChanged", passwordChanged);

        return "login.html";
    }

    @RequestMapping("/validation")
    public String displayValidationPage(){
        return "validation.html";
    }

    @RequestMapping("/regenaratetoken")
    public String displayRegenaratetokenPage(Model model,
                                             @RequestParam(value = "tokenExpired", required = false) boolean tokenExpired){
        model.addAttribute("tokenExpired", tokenExpired);
        return "regenaratetoken.html";
    }
    @PostMapping("/newToken")
    public String displayNewTokenPage(@RequestParam("email") String email){
        authenticationService.regenerateToken(email);
        return "redirect:/validation";
    }

    @RequestMapping("/forgotPass")
    public String displayForgotPassPage(){
        return "forgotpassword.html";
    }

    @PostMapping("/newPassword")
    public String newPassword(@RequestParam("email") String email, HttpSession session){
        session.setAttribute("emailPassRecover", email);
        authenticationService.recoverPassword(email);
        return "redirect:/validation";
    }

    @RequestMapping("/ChangePassword")
    public String displayChangePasswordPage(Model model){
        model.addAttribute("newPassword", new String());
        return "changePassword.html";
    }

    @PostMapping("/changePass")
    public String ChangePassword(@ModelAttribute("newPassword") String newPassword, HttpSession session){
        String email = (String) session.getAttribute("emailPassRecover");
        authenticationService.changePassword(email, newPassword);
        session.removeAttribute("emailPassRecover");
        return "redirect:/login?passwordChanged=true";
    }

}
