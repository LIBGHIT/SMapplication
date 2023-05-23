package com.smartmaint.web.Confirmation.Email;

public interface EmailSender {
    void send(String to, String email);
}
