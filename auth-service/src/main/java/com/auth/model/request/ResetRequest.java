package com.auth.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetRequest {
    private String oldPassword;
    private String newPassword;
}