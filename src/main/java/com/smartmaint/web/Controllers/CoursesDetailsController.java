package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CoursesDetailsController {
    @RequestMapping("/coursesdetails")
    public String displayCoursesDetailsPage(){ return "coursesdetails.html";
    }
}
