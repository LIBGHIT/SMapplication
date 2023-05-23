package com.smartmaint.web.Controllers;

import com.smartmaint.web.Auth.AuthenticationRequest;
import com.smartmaint.web.Auth.AuthenticationService;
import com.smartmaint.web.Models.User;
import com.smartmaint.web.Services.ConfirmationTokenService;
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
                                      @RequestParam(value = "validationSuccess", required = false) boolean validationSuccess) {

        model.addAttribute("loginRequest", new AuthenticationRequest());
        model.addAttribute("alreadyConfirmed", alreadyConfirmed);
        model.addAttribute("tokenExpired", tokenExpired);
        model.addAttribute("emailOrPassIncorrect", emailOrPassIncorrect);
        model.addAttribute("emailNotValidated", emailNotValidated);
        model.addAttribute("validationSuccess", validationSuccess);

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
        log.info("test1 email value >>> {}",email);
        authenticationService.regenerateToken(email);
        log.info("test2 email value >>> {}",email);
        return "redirect:/validation";
    }





}
