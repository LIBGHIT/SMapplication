package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CoursesDashboardController {

    @RequestMapping("/coursesdashboard")
    public String displayCoursesDashboardPage(){
        return "coursesdashboard.html";
    }
}
