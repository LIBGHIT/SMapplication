package com.smartmaint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class  PaymentController {
    @RequestMapping("/payment")
    public String displayPaymentPage(){ return "payment.html";
    }
}
