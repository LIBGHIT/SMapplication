package com.smartmaint.web.Models;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@AllArgsConstructor
@Builder
@NoArgsConstructor(force = true)
@Data
@Document(collection = "Blogs")
public class Blogs {
    @Id
    private String id_blog;
    @NonNull
    @NotBlank(message = "Title must be filled")
    @Size(min = 3, message = "Title must be at least 3 characters long")
    private String title;
    @NonNull
    @NotBlank(message = "Subject must be filled")
    @Size(min = 3, message = "Subject must be at least 3 characters long")
    private String subject;
    @NonNull
    @NotBlank(message = "Excerpt must be filled")
    @Size(min = 3, max = 700, message = "Excerpt must be at least 3 characters long and 700 at most")
    private String excerpt;
    @NonNull
    @NotBlank(message = "Body must be filled")
    @Size(min = 3, max = 2500, message = "Body must be at least 3 characters long and 2500 at most")
    private String body;

    private Date date;
    private boolean archived;
    private String imageId;
}





