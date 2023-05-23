package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProjectsController {

    @RequestMapping("/projects")
    public String displayProjectsPage(){
        return "projects.html";
    }
}
