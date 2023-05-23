package com.smartmaint.web.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class  AuthenticationResponse {

    private String jwtToken;
    private String confirmationToken;

}
