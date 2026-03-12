package com.gym_project.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AuthService {

    public void authenticate(String username, Role role) {
        log.debug("Authenticating username='{}' with role={}", username, role);

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(role.asAuthority()));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Authenticated: username='{}', role={}", username, role);
    }

    public void logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? (String) auth.getPrincipal() : "unknown";

        SecurityContextHolder.clearContext();

        log.info("Logged out: username='{}'", username);
    }
}