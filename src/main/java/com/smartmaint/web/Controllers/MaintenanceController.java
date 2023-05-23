package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class  MaintenanceController {
    @RequestMapping("/maintenance")
    public String displayMaintenancePage(){ return "maintenance.html";
    }
}
