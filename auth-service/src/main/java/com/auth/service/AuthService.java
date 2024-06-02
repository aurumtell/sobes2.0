package com.auth.service;

import com.auth.exception.AuthorizationException;
import com.auth.exception.BusinessException;
import com.auth.exception.MyEntityNotFoundException;
import com.auth.model.entity.LevelEntity;
import com.auth.model.entity.RoleEntity;
import com.auth.model.request.EmailRequest;
import com.auth.model.request.ResetRequest;
import com.auth.model.response.MessageResponse;
import com.auth.model.entity.RefreshToken;
import com.auth.model.entity.UserEntity;
import com.auth.model.request.AuthRequest;
import com.auth.model.request.SignUpRequest;
import com.auth.model.response.JwtResponse;
import com.auth.model.response.ProfileResponse;
import com.auth.repo.RoleRepository;
import com.auth.repo.UserRepository;
import com.auth.security.jwt.JwtUtils;
import com.auth.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleResult;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder encoder;

    public ResponseEntity<?> authenticateUser(AuthRequest emailRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailRequest.getEmail(), emailRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getEmail(), userDetails.getUsername(), userDetails.getAuthorities().toString()));
    }

    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest) {
        UserEntity user = userRepository.findByEmail(signUpRequest.getEmail()).orElseThrow(() -> new MyEntityNotFoundException("email"));
        if (!user.isVerified()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email not verified"));
        }
        if (user.getUsername() != null && user.getPasswordHash() != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("User already registered!"));
        }
        user.setUsername(signUpRequest.getUsername());
        user.setPasswordHash(encoder.encode(signUpRequest.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());

        user.setProfessions(new HashSet<>());
        user.setCompanies(new HashSet<>());
        if (user.getRole() == null) {
            user.setRole(roleRepository.findByRole("ROLE_USER").get());
        }
        userRepository.save(user);
        return authenticateUser(new AuthRequest(user.getEmail(), signUpRequest.getPassword()));
    }

    public void createAndSendConfirmationCode(UserEntity user) {
        String code = generateConfirmationCode();
        user.setConfirmationCode(code);
        user.setConfirmationCodeSentAt(LocalDateTime.now());
        user.setVerified(false);
        emailService.sendRegistrationConfirmationEmail(user.getEmail(), code);
        userRepository.save(user);
    }

    public ResponseEntity<?> verifyConfirmationCode(UserEntity user, String code) {
        if (user.getConfirmationCode().equals(code) &&
                user.getConfirmationCodeSentAt().isAfter(LocalDateTime.now().minusMinutes(15))) {
            user.setVerified(true);

            userRepository.save(user);
            return ResponseEntity.ok().body(new MessageResponse("Email successfully verified!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Confirmation code is incorrect!"));
        }
    }

    private String generateConfirmationCode() {
        // Генерация кода
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    public ResponseEntity<?> registerEmail(EmailRequest emailRequest) {
        System.out.println(userRepository.existsByEmail(emailRequest.getEmail()));
        if (userRepository.existsByEmail(emailRequest.getEmail())) {
            if (userRepository.findByEmail(emailRequest.getEmail()).get().isVerified()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already taken!"));
            } else {
                createAndSendConfirmationCode(userRepository.findByEmail(emailRequest.getEmail()).get());
                return ResponseEntity.ok().body(new MessageResponse("Email message sent successfully!"));
            }
        }
        UserEntity user = new UserEntity();
        user.setEmail(emailRequest.getEmail());
//        user.setRole(roleRepository.findByRole("ROLE_USER").get());
        createAndSendConfirmationCode(user);
        return ResponseEntity.ok().body(new MessageResponse("Email message sent successfully!"));
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new MyEntityNotFoundException(email));
    }

    public ResponseEntity<?> resetPassword(String oldPassword, String newPassword, UserDetailsImpl user) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new MyEntityNotFoundException(user.getId()));

        if (!encoder.matches(oldPassword, userEntity.getPasswordHash())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Old password is invalid!"));
        }
        userEntity.setPasswordHash(encoder.encode(newPassword));
        userRepository.save(userEntity);
        return ResponseEntity.ok().body(new MessageResponse("Password changed successfully!"));
    }

    public ResponseEntity<?> recoveryAccountEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email does not exist!"));
        }
        UserEntity user = findUserByEmail(email);
        createAndSendConfirmationCode(user);
        return ResponseEntity.ok().body(new MessageResponse("Email message sent successfully!"));
    }

    @Transactional
    public ProfileResponse recoveryAccount(String email, String password) {
        if (!userRepository.existsByEmail(email)) {
            throw new MyEntityNotFoundException("email");
        }
        UserEntity user = findUserByEmail(email);
        user.setPasswordHash(encoder.encode(password));
        userRepository.save(user);
        return new ProfileResponse(user);
    }
}