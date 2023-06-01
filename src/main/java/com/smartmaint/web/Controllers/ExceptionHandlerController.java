package com.smartmaint.web.Controllers;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
//
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException ex) {
        if (ex.getMessage().equals("email already confirmed")) {
            return "redirect:/login?alreadyConfirmed=true";
        }else if (ex.getMessage().equals("CV file is more than 5MB")) {
            return "redirect:/register?cvSize=true";
        }else if (ex.getMessage().equals("Invalid file type. Only PDF or image files are allowed.")) {
            return "redirect:/register?typeNonValid=true";
        } else if (ex.getMessage().equals("Invalid email or password!")) {
            return "redirect:/login?emailOrPassIncorrect=true";
        } else if (ex.getMessage().equals("token expired")) {
            return "redirect:/regenaratetoken?tokenExpired=true";
        } else if (ex.getMessage().equals("Email is not validated!")) {
            return "redirect:/login?emailNotValidated=true";
        } else if (ex.getMessage().equals("Invalid Token pass!")) {
            return "redirect:/login?passTokenUsed=true";
        } else if (ex.getMessage().equals("token expired pass!")) {
            return "redirect:/login?passTokenExpired=true";
        }
        return "/home";
    }

//    @ExceptionHandler(UsernameNotFoundException.class)
//    public String handleUsernameNotFoundException(UsernameNotFoundException ex) {
//        if (ex.getMessage().equals("User not found !")) {
//            return "/login?emailOrPassIncorrect=true" ;
//        }
//        return "/home" ;
//    }

}
