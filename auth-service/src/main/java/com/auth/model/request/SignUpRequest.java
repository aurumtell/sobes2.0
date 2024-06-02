package com.auth.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
public class SignUpRequest {
    private String email;
    private String password;
    private String username;
}