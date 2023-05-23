package com.smartmaint.web.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class CoursesController {
    @RequestMapping("/courses")
    public String displayCoursesPage(){
        return "courses.html";
    }
}
