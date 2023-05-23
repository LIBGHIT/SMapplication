package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DetailserviceController {
    @RequestMapping("/detailservice")
    public String displayDetailsServicesPage(){
        return "detailservice.html";
    }
}
