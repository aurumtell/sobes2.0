package com.auth.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String email;
    private String username;
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private String role;

    public JwtResponse(String accessToken, String refreshToken, Long id, String email, String username, String role) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
    }
}
