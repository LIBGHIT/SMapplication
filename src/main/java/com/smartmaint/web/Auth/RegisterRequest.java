package com.smartmaint.web.Auth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class RegisterRequest {
    private String fisrtName;
    private String lastName;
    private String email;
    private String password;

}
