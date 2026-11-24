package com.example.leviathan.common.security;

import com.example.leviathan.common.exception.UnauthorizedException;
import com.example.leviathan.common.resolver.LoginIdArgumentResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoginIdArgumentResolverTest {

    private final LoginIdArgumentResolver resolver = new LoginIdArgumentResolver();

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("인증된 사용자의 memberId를 반환한다")
    void resolve_Authenticated() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                "testUser", null, List.of(new SimpleGrantedAuthority("ROLE_MEMBER"))));

        assertEquals("testUser", resolver.resolveArgument(null, null, null, null));
    }

    @Test
    @DisplayName("인증 정보가 없으면 UnauthorizedException")
    void resolve_NoAuthentication() {
        assertThrows(UnauthorizedException.class, () -> resolver.resolveArgument(null, null, null, null));
    }

    @Test
    @DisplayName("익명 사용자면 UnauthorizedException")
    void resolve_Anonymous() {
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken(
                "key", "anonymousUser", List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));

        assertThrows(UnauthorizedException.class, () -> resolver.resolveArgument(null, null, null, null));
    }
}
