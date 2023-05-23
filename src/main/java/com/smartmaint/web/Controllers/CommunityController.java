package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommunityController {

    @RequestMapping("/community")
    public String displayCommunityPage(){
        return "community.html";
    }
}
