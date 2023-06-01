package com.smartmaint.web.Controllers;


import com.smartmaint.web.Auth.AuthenticationRequest;
import com.smartmaint.web.Auth.AuthenticationResponse;
import com.smartmaint.web.Auth.AuthenticationService;
import com.smartmaint.web.Models.User;
import com.smartmaint.web.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;

    @PostMapping("/registerUser")
    public String register(Model model, @Valid @ModelAttribute("user") User user, Errors errors, @RequestParam(value = "cvFile", required = false) MultipartFile cvFile) throws IOException {
        if (errors.hasErrors()){
            return "/register";
        }

        if (userService.emailExists(user.getEmail())){
            model.addAttribute("checkEmail", true);
            return "/register";
        }

        if (user.getPassword().equals(user.getCheckPass()) != true){
            model.addAttribute("checkPassResult", true);
            return "/register";
        }

        AuthenticationResponse JWTToken = service.register(user, cvFile);
        return "redirect:/validation";
    }

    @GetMapping("/registrationConfirm")
    public String confirm(@RequestParam("confirmToken") String token) {
        service.confirmToken(token);
        return "redirect:/login?validationSuccess=true";
    }

    @GetMapping("/forgotPasswordRecovery")
    public String confirmPass(@RequestParam("confirmToken") String token, HttpSession session) {
        service.confirmPassToken(token, session);
        return "redirect:/ChangePassword";
    }

    @PostMapping("/authenticate")
    public String authenticate(@ModelAttribute("loginRequest") AuthenticationRequest authenticationRequest,Model model, HttpSession httpSession){
        String checkPoint = (String) httpSession.getAttribute("checkPoint");

        AuthenticationResponse authenticationResponse = service.authenticate(authenticationRequest);
        httpSession.setAttribute("JwtToken", authenticationResponse.getJwtToken());

        if(checkPoint != null && !checkPoint.isEmpty() ){
            httpSession.removeAttribute("checkPoint");
            return checkPoint;
        }
        return "redirect:/home";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Invalidate the session
        request.getSession().invalidate();

        // Clear the authentication context
        SecurityContextHolder.getContext().setAuthentication(null);

        // Redirect to the login page or another appropriate page
        return "redirect:/home";
    }

}
