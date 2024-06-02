//package com.auth.controller;
//
//import com.auth.model.entity.RefreshToken;
//import com.auth.model.entity.UserEntity;
//import com.auth.model.request.*;
//import com.auth.model.response.JwtResponse;
//import com.auth.model.response.MessageResponse;
//import com.auth.model.response.TokenRefreshResponse;
//import com.auth.security.jwt.JwtUtils;
//import com.auth.security.services.UserDetailsImpl;
//import com.auth.service.AuthService;
//import com.auth.service.RefreshTokenService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.when;
//import static org.springframework.http.ResponseEntity.ok;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(AuthController.class)
//public class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthService authService;
//
//    @MockBean
//    private RefreshTokenService refreshTokenService;
//
//    @MockBean
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(SecurityMockMvcConfigurers.springSecurity())
//                .build();
//    }
//
//    @Test
//    void authenticateUser() throws Exception {
//        AuthRequest authRequest = new AuthRequest("test@example.com", "password");
//        JwtResponse jwtResponse = new JwtResponse("token", "refreshToken", 1L, "test@example.com", "testUser", "ROLE_USER");
//
//        doReturn(ok(jwtResponse)).when(authService.authenticateUser(any(AuthRequest.class)));
//
//        mockMvc.perform(post("/auth/signin")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"test@example.com\", \"password\":\"password\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("token"));
//    }
//
//    @Test
//    void registerUser() throws Exception {
//        SignUpRequest signUpRequest = new SignUpRequest("testUser", "test@example.com", "password");
//        JwtResponse jwtResponse = new JwtResponse("token", "refreshToken", 1L, "test@example.com", "testUser", "ROLE_USER");
//
//        doReturn(ok(jwtResponse)).when(authService.registerUser(any(SignUpRequest.class)));
//
//        mockMvc.perform(post("/auth/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\":\"testUser\", \"email\":\"test@example.com\", \"password\":\"password\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("token"));
//    }
//
//    @Test
//    void emailUser() throws Exception {
//        MessageResponse messageResponse = new MessageResponse("Email message sent successfully!");
//
//        doReturn(ok(messageResponse)).when(authService.registerEmail(any(EmailRequest.class)));
//
//        mockMvc.perform(post("/auth/email")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"test@example.com\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Email message sent successfully!"));
//    }
//
//    @Test
//    void verifyCode() throws Exception {
//        UserEntity user = new UserEntity();
//        MessageResponse messageResponse = new MessageResponse("Email successfully verified!");
//
//        when(authService.findUserByEmail(anyString())).thenReturn(user);
//        doReturn(ok(messageResponse)).when(authService.verifyConfirmationCode(any(UserEntity.class), anyString()));
//
//        mockMvc.perform(post("/auth/verify")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"test@example.com\", \"code\":\"123456\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Email successfully verified!"));
//    }
//
//    @Test
//    @WithMockUser
//    void resetPassword() throws Exception {
//        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@example.com", "password", "testUser", null);
//        MessageResponse messageResponse = new MessageResponse("Password changed successfully!");
//
//        doReturn(ok(messageResponse)).when(authService.resetPassword(anyString(), anyString(), any(UserDetailsImpl.class)));
//
//        mockMvc.perform(post("/auth/reset")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"oldPassword\":\"oldPassword\", \"newPassword\":\"newPassword\"}")
//                        .principal(() -> String.valueOf(userDetails)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Password changed successfully!"));
//    }
//
//    @Test
//    void recoveryAccountEmail() throws Exception {
//        MessageResponse messageResponse = new MessageResponse("Email message sent successfully!");
//
//        doReturn(ok(messageResponse)).when(authService.recoveryAccountEmail(String.valueOf(any(EmailRequest.class))));
//
//        mockMvc.perform(post("/auth/recovery/email")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"test@example.com\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Email message sent successfully!"));
//    }
//
//    @Test
//    void recoveryAccount() throws Exception {
//        MessageResponse messageResponse = new MessageResponse("Password changed successfully!");
//
//        doReturn(ok(messageResponse)).when(authService.recoveryAccount(anyString(), anyString()));
//
//        mockMvc.perform(post("/auth/recovery")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"test@example.com\", \"password\":\"newPassword\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Password changed successfully!"));
//    }
//
//    @Test
//    void refreshtoken() throws Exception {
//        RefreshToken refreshToken = new RefreshToken();
//        refreshToken.setToken("refreshToken");
//        refreshToken.setUser(new UserEntity());
//
//        when(refreshTokenService.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
//        when(refreshTokenService.verifyExpiration(any(RefreshToken.class))).thenReturn(refreshToken);
//        when(jwtUtils.generateTokenFromEmail(anyString())).thenReturn("newToken");
//
//        mockMvc.perform(post("/auth/refreshtoken")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"refreshToken\":\"refreshToken\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("newToken"));
//    }
//
//    @Test
//    @WithMockUser
//    void logoutUser() throws Exception {
//        MessageResponse messageResponse = new MessageResponse("User logged out successfully!");
//
//        doReturn(ok(messageResponse)).when(refreshTokenService.logout(any(UserDetailsImpl.class)));
//
//        mockMvc.perform(get("/user/logout"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("User logged out successfully!"));
//    }
//}
