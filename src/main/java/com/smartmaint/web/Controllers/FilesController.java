package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FilesController {

    @RequestMapping("/files")
    public String displayFilesPage(){
        return "files.html";
    }
}
