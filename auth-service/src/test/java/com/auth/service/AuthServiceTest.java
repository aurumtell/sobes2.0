//package com.auth.service;
//
//import com.auth.exception.MyEntityNotFoundException;
//import com.auth.model.entity.RefreshToken;
//import com.auth.model.entity.RoleEntity;
//import com.auth.model.entity.UserEntity;
//import com.auth.model.request.AuthRequest;
//import com.auth.model.request.EmailRequest;
//import com.auth.model.request.SignUpRequest;
//import com.auth.model.response.JwtResponse;
//import com.auth.model.response.MessageResponse;
//import com.auth.repo.RoleRepository;
//import com.auth.repo.UserRepository;
//import com.auth.security.jwt.JwtUtils;
//import com.auth.security.services.UserDetailsImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class AuthServiceTest {
//
//    @InjectMocks
//    private AuthService authService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private RoleRepository roleRepository;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @Mock
//    private JwtUtils jwtUtils;
//
//    @Mock
//    private RefreshTokenService refreshTokenService;
//
//    @Mock
//    private EmailService emailService;
//
//    @Mock
//    private PasswordEncoder encoder;
//
//    private RoleEntity roleUser;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        roleUser = new RoleEntity(1L, "ROLE_USER", null);
//        when(roleRepository.findByRole("ROLE_USER")).thenReturn(Optional.of(roleUser));
//    }
//
//    @Test
//    void authenticateUserTest() {
//        AuthRequest authRequest = new AuthRequest("test@example.com", "password");
//        UserEntity user = new UserEntity();
//        user.setId(1L);
//        user.setEmail("test@example.com");
//        user.setPasswordHash("encodedPassword");
//        user.setRole(roleUser);
//
//        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
//        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("jwtToken");
//        when(refreshTokenService.createRefreshToken(anyLong())).thenReturn(new RefreshToken());
//
//        ResponseEntity<?> responseEntity = authService.authenticateUser(authRequest);
//
//        assertNotNull(responseEntity);
//        assertTrue(responseEntity.getBody() instanceof JwtResponse);
//        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
//        assertEquals("jwtToken", jwtResponse.getToken());
//    }
//
//    @Test
//    void registerUserTest() {
//        SignUpRequest signUpRequest = new SignUpRequest("testUser", "test@example.com", "password");
//        UserEntity user = new UserEntity();
//        user.setId(1L);
//        user.setEmail("test@example.com");
//        user.setVerified(true);
//        user.setRole(roleUser);
//
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
//        when(encoder.encode(anyString())).thenReturn("encodedPassword");
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(UserDetailsImpl.build(user), null);
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
//        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("jwtToken");
//        when(refreshTokenService.createRefreshToken(anyLong())).thenReturn(new RefreshToken());
//
//        ResponseEntity<?> responseEntity = authService.registerUser(signUpRequest);
//
//        assertNotNull(responseEntity);
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void verifyConfirmationCodeTest() {
//        UserEntity user = new UserEntity();
//        user.setConfirmationCode("123456");
//        user.setConfirmationCodeSentAt(LocalDateTime.now());
//
//        ResponseEntity<?> responseEntity = authService.verifyConfirmationCode(user, "123456");
//
//        assertNotNull(responseEntity);
//        assertTrue(responseEntity.getBody() instanceof MessageResponse);
//        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
//        assertEquals("Email successfully verified!", messageResponse.getMessage());
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void registerEmailTest() {
//        EmailRequest emailRequest = new EmailRequest("test@example.com");
//
//        when(userRepository.existsByEmail(anyString())).thenReturn(false);
//
//        ResponseEntity<?> responseEntity = authService.registerEmail(emailRequest);
//
//        assertNotNull(responseEntity);
//        assertTrue(responseEntity.getBody() instanceof MessageResponse);
//        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
//        assertEquals("Email message sent successfully!", messageResponse.getMessage());
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//    }
//
//    @Test
//    void resetPasswordTest() {
//        UserEntity user = new UserEntity();
//        user.setId(1L);
//        user.setPasswordHash("oldPassword");
//        user.setRole(roleUser);
//
//        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
//
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
//        when(encoder.matches(anyString(), anyString())).thenReturn(true);
//        when(encoder.encode(anyString())).thenReturn("newEncodedPassword");
//
//        ResponseEntity<?> responseEntity = authService.resetPassword("oldPassword", "newPassword", userDetails);
//
//        assertNotNull(responseEntity);
//        assertTrue(responseEntity.getBody() instanceof MessageResponse);
//        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
//        assertEquals("Password changed successfully!", messageResponse.getMessage());
//        verify(userRepository, times(1)).save(user);
//    }
//}
