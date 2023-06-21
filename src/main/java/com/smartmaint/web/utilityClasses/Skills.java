package com.smartmaint.web.utilityClasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class Skills {
    private String skill_1;
    private String skill_2;
    private String skill_3;
    private String skill_4;
    private String skill_1_experience;
    private String skill_2_experience;
    private String skill_3_experience;
    private String skill_4_experience;
    //getters and setters
}
