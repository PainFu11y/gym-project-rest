package com.gym_project.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionAuthFilterTest {

    private static final String AUTH_SESSION_KEY = "SPRING_SECURITY_AUTHENTICATION";

    @InjectMocks
    private SessionAuthFilter sessionAuthFilter;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldRestoreAuthFromSession_whenSessionHasAuth() throws Exception {
        Authentication storedAuth = new UsernamePasswordAuthenticationToken(
                "John.Smith", null, List.of(new SimpleGrantedAuthority("ROLE_TRAINER")));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession(true).setAttribute(AUTH_SESSION_KEY, storedAuth);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        sessionAuthFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .isEqualTo("John.Smith");
    }

    @Test
    void doFilterInternal_shouldNotOverwriteExistingAuth_whenContextAlreadyHasAuth() throws Exception {
        Authentication existingAuth = new UsernamePasswordAuthenticationToken(
                "existing-user", null, List.of(new SimpleGrantedAuthority("ROLE_TRAINEE")));
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        Authentication sessionAuth = new UsernamePasswordAuthenticationToken(
                "session-user", null, List.of(new SimpleGrantedAuthority("ROLE_TRAINER")));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession(true).setAttribute(AUTH_SESSION_KEY, sessionAuth);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        sessionAuthFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .isEqualTo("existing-user");
    }

    @Test
    void doFilterInternal_shouldNotSetAuth_whenSessionIsNull() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        sessionAuthFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternal_shouldNotSetAuth_whenSessionHasNoAuthAttribute() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession(true);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        sessionAuthFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternal_shouldSaveAuthToSession_afterFilterChain() throws Exception {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "Jane.Doe", null, List.of(new SimpleGrantedAuthority("ROLE_TRAINEE")));

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        MockFilterChain filterChain = new MockFilterChain() {
            @Override
            public void doFilter(javax.servlet.ServletRequest req,
                                 javax.servlet.ServletResponse res)
                    throws java.io.IOException, javax.servlet.ServletException {
                SecurityContextHolder.getContext().setAuthentication(auth);
                super.doFilter(req, res);
            }
        };

        sessionAuthFilter.doFilterInternal(request, response, filterChain);

        Authentication saved = (Authentication) request.getSession(false)
                .getAttribute(AUTH_SESSION_KEY);
        assertThat(saved).isNotNull();
        assertThat(saved.getPrincipal()).isEqualTo("Jane.Doe");
    }

    @Test
    void doFilterInternal_shouldNotSaveToSession_whenAuthIsNull() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        sessionAuthFilter.doFilterInternal(request, response, filterChain);

        assertThat(request.getSession(false)).isNull();
    }

    @Test
    void doFilterInternal_shouldAlwaysCallFilterChain() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = mock(MockFilterChain.class);

        sessionAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}