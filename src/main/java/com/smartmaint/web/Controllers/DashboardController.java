package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController {

    @RequestMapping("/dashboard")
    public String displayDashboardPage(){
        return "dashboard.html";
    }
}
