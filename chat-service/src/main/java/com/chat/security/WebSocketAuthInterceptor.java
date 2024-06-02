package com.chat.security;

import com.chat.security.jwt.JwtUtils;
import com.chat.security.services.UserDetailsImpl;
import com.chat.security.services.UserDetailsServiceImpl;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public WebSocketAuthInterceptor(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
        System.out.println("preSend: Authorization header = " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            if (jwtToken.isEmpty()) {
                System.out.println("JWT token is empty");
                return null;
            }

            if (jwtUtils.validateJwtToken(jwtToken)) {
                String email = jwtUtils.getEmailFromJwtToken(jwtToken);
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                accessor.setUser(authentication);
                System.out.println("interceptor: User authenticated");
                return message;
            } else {
                System.out.println("JWT token validation failed");
            }
        } else {
            System.out.println("Authorization header is missing or does not start with Bearer");
        }
        return message;
    }

}
