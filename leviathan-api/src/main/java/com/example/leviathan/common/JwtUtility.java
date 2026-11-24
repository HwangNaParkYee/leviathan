package com.example.leviathan.common;

import com.example.leviathan.member.db.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Service
public class JwtUtility {

    private final SecretKey secretKey; // JWT 서명에 사용되는 비밀 키 // 생성한 비밀 키의 타입이 SecretKey 타입


    // ⏳ Access Token 만료 시간 (1분)
    public static final Duration ACCESS_TOKEN_VALIDITY = Duration.ofMinutes(1);

    // ⏳ Refresh Token 만료 시간 (1시간)
    public static final Duration REFRESH_TOKEN_VALIDITY = Duration.ofHours(1);

    // JWT 서명에 사용되는 비밀 키 생성
    public JwtUtility(@Value("${jwt.base64Secret}") String base64Secret) { // @Value을 통해 application.yml에서 값 주입
        this.secretKey = Keys.hmacShaKeyFor(base64Secret.getBytes());
    }                                                    // base64Secret에 64바이트 이상이면 자동으로 HS512 알고리즘 사용

    /**
     * 토큰 생성 (role 포함) 메서드
     */
    public String generateToken(String memberId, Role role) {
        return Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY.toMillis()))
                .addClaims(Map.of("role", role.name()))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 토큰 검증 및 파싱 메서드
     */
    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Token 유효 여부 확인 메소드
     */
    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Refresh Token 생성 메서드
     */
    public String generateRefreshToken(String userEmail) {
        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY.toMillis()))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Token → memberId 추출
     */
    public String getMemberIdFromToken(String token) {
        try {
            return validateToken(token).getSubject();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에서도 클레임을 가져오기 위해 getClaims() 사용
            return e.getClaims().getSubject();
        }
    }

    /**
     * Cookie → accessToken 추출
     */
    public String extractAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AccessTokenCookie.NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
