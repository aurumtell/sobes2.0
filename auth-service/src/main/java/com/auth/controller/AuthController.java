package com.auth.controller;

import com.auth.exception.TokenRefreshException;
import com.auth.model.entity.RefreshToken;
import com.auth.model.entity.UserEntity;
import com.auth.model.request.*;
import com.auth.model.response.MessageResponse;
import com.auth.model.response.TokenRefreshResponse;
import com.auth.security.jwt.JwtUtils;
import com.auth.security.services.UserDetailsImpl;
import com.auth.service.AuthService;
import com.auth.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(description = "Api to manage authorization",
        name = "User Resource")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtUtils jwtUtils;

    @Operation(summary = "auth",
            description = "Create article")
    @PostMapping(value = "/auth/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest emailRequest) {
        return authService.authenticateUser(emailRequest);
    }

    @PostMapping(value = "/auth/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @PostMapping(value = "/auth/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> emailUser(@Valid @RequestBody EmailRequest emailRequest) {
        return authService.registerEmail(emailRequest);
    }

    @PostMapping("/auth/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest request) {
        UserEntity user = authService.findUserByEmail(request.getEmail());
        return authService.verifyConfirmationCode(user, request.getCode());
    }

    @PostMapping("/auth/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetRequest request, @AuthenticationPrincipal UserDetailsImpl user) {
        return authService.resetPassword(request.getOldPassword(), request.getNewPassword(), user);
    }

    @PostMapping("/auth/recovery/email")
    public ResponseEntity<?> recoveryAccountEmail(@RequestBody EmailRequest request) {
        return authService.recoveryAccountEmail(request.getEmail());
    }

    @PostMapping("/auth/recovery")
    public ResponseEntity<?> recoveryAccount(@RequestBody AuthRequest authRequest) {
        return authService.recoveryAccount(authRequest.getEmail(), authRequest.getPassword());
    }


    @PostMapping(value = "/auth/refreshtoken",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromEmail(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @GetMapping("/user/logout")
    public ResponseEntity<?> logoutUser(@AuthenticationPrincipal UserDetailsImpl user) {
        return refreshTokenService.logout(user);
    }
}
