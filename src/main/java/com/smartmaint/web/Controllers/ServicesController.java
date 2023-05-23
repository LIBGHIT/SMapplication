package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ServicesController {

    @RequestMapping("/services")
    public String displayServicesPage(){
        return "services.html";
    }
}
