package com.smartmaint.web.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Builder
@NoArgsConstructor(force = true)
@Data
@Document(collection = "Skills")
public class Skill {
    @Id
    private String id;
    private String name;
}
