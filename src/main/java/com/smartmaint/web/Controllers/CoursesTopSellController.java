package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CoursesTopSellController {
    @RequestMapping("/coursestopsell")
    public String displayTopSellPage(){
        return "coursestopsell.html";
    }
}
