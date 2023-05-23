package com.smartmaint.web.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@Builder
@NoArgsConstructor(force = true)
@Data
@Document(collection = "Comments")
public class Comment {
    @Id
    private String id;
    @NonNull
    @NotBlank(message = "Text must be filled")
    @Size(min = 1, message = "Text must be at least 1 character long")
    private String text;
    private Date date;
    private String userId;
    private String blogId;
    private String parentCommentId;
    private String firstName;
    private String lastName;

    @DBRef
    private List<Comment> replies = new ArrayList<>();
}