package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ComingSoonController {

    @RequestMapping("/comingsoon")
    public String displayComingSoonPage(){
        return "comingsoon.html";
    }
}
