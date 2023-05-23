package com.smartmaint.web.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "Contacts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    @Id
    private String id_message;

    @NotBlank(message = "First name must be filled")
    @Size(min = 3, message = "First name must be at least 3 characters long")
    private String firstName;

    @NotBlank(message = "Last name must be filled")
    @Size(min = 3, message = "Last name must be at least 3 characters long")
    private String lastName;

    @NotBlank(message = "Email must be filled")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Number must be filled")
    private String phoneNumber;

    @NotBlank(message = "Subject must be filled")
    @Size(min = 3, max = 256, message = "Subject must be at least 3 characters long and 256 at most")
    private String subject;

    @NotBlank(message = "Message must be filled")
    @Size(min = 3, max = 1000, message = "Message must be at least 3 characters long and 1000 at most")
    private String message;

    private boolean read;

    private Date dateSent; // to store the time when the message was sent or received

}