package com.example.leviathan.common.security;

import com.example.leviathan.common.JwtUtility;
import com.example.leviathan.member.db.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilityTest {

    private static final String SECRET = "test-secret-key-test-secret-key-test-secret-key-test-secret-key-1234";

    private final JwtUtility jwtUtility = new JwtUtility(SECRET);

    @Test
    @DisplayName("발급한 토큰은 유효하고 memberId와 role을 담고 있다")
    void generateToken_Valid() {
        String token = jwtUtility.generateToken("testUser", Role.ROLE_MEMBER);

        Claims claims = jwtUtility.validateToken(token);

        assertTrue(jwtUtility.isTokenValid(token));
        assertEquals("testUser", claims.getSubject());
        assertEquals("ROLE_MEMBER", claims.get("role", String.class));
    }

    @Test
    @DisplayName("만료된 토큰은 유효하지 않지만 memberId는 추출할 수 있다")
    void expiredToken() {
        String expired = Jwts.builder()
                .setSubject("testUser")
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();

        assertThrows(ExpiredJwtException.class, () -> jwtUtility.validateToken(expired));
        assertFalse(jwtUtility.isTokenValid(expired));
        assertEquals("testUser", jwtUtility.getMemberIdFromToken(expired));
    }

    @Test
    @DisplayName("서명이 다르거나 형식이 잘못된 토큰은 유효하지 않다")
    void invalidToken() {
        String forged = Jwts.builder()
                .setSubject("testUser")
                .signWith(Keys.hmacShaKeyFor("another-secret-key-another-secret-key-another-secret-key-12345678".getBytes()))
                .compact();

        assertFalse(jwtUtility.isTokenValid(forged));
        assertFalse(jwtUtility.isTokenValid("not-a-jwt"));
        assertFalse(jwtUtility.isTokenValid(null));
    }
}
