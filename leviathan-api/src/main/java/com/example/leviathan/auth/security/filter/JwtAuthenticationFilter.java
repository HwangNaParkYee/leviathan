package com.example.leviathan.auth.security.filter;

import com.example.leviathan.common.exception.UnauthorizedException;
import com.example.leviathan.common.AccessTokenCookie;
import com.example.leviathan.common.JwtUtility;
import com.example.leviathan.common.TokenRefreshService;
import com.example.leviathan.member.db.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final TokenRefreshService tokenRefreshService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/login") ||
                path.startsWith("/api/signUp") ||
                path.startsWith("/api/public/findByAllProblem") ||
                path.startsWith("/api/comments/article") ||
                path.startsWith("/api/prompt") ||
                path.startsWith("/api/reviews/problem/")||
                path.startsWith("/api/reviews/all")||
                path.startsWith("/api/reviews/problem-statistics/") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtility.extractAccessTokenFromCookie(request);

        if (token == null) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "토큰이 제공되지 않았습니다.");
            return;
        }

        Claims claims;
        try {
            claims = jwtUtility.validateToken(token);
        } catch (ExpiredJwtException e) {
            try {
                String newToken = tokenRefreshService.reissueAccessToken(token);
                response.addHeader(HttpHeaders.SET_COOKIE, AccessTokenCookie.of(newToken).toString());
                claims = jwtUtility.validateToken(newToken);
            } catch (UnauthorizedException ex) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "AccessToken 및 RefreshToken이 만료되었습니다.");
                return;
            }
        } catch (JwtException | IllegalArgumentException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다.");
            return;
        }

        Role role;
        try {
            role = Role.valueOf(claims.get("role", String.class));
        } catch (IllegalArgumentException | NullPointerException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다.");
            return;
        }

        authenticateUser(claims.getSubject(), role);

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    private void authenticateUser(String memberId, Role role) {
        List<SimpleGrantedAuthority> authorities = role.getAuthorityNames().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                memberId,
                null,
                authorities
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
