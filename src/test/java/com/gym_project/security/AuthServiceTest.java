package com.gym_project.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticate_shouldSetAuthenticationInSecurityContext() {
        authService.authenticate("John.Smith", Role.TRAINER);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertThat(auth).isNotNull();
        assertThat(auth.getPrincipal()).isEqualTo("John.Smith");
        assertThat(auth.getCredentials()).isNull();
        assertThat(auth.isAuthenticated()).isTrue();
    }


    @Test
    void logout_shouldClearSecurityContext() {
        authService.authenticate("John.Smith", Role.TRAINER);

        authService.logout();

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void logout_shouldNotThrow_whenNoAuthenticationPresent() {
        SecurityContextHolder.clearContext();

        org.assertj.core.api.Assertions.assertThatCode(() -> authService.logout())
                .doesNotThrowAnyException();
    }
}