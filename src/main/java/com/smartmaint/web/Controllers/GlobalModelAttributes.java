package com.smartmaint.web.Controllers;

import com.smartmaint.web.Services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @Autowired
    private ContactService contactService;

    @ModelAttribute("countUnread")
    public long getCountUnread() {
        return contactService.countUnreadMessages();
    }
}
