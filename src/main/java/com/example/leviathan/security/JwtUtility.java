package com.example.leviathan.security;

import com.example.leviathan.domain.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtUtility {

    private final SecretKey secretKey; // JWT 서명에 사용되는 비밀 키 // 생성한 비밀 키의 타입이 SecretKey 타입


    // ⏳ Access Token 만료 시간 (30분)
    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 1; // 1분

    // ⏳ Refresh Token 만료 시간 (24시간)
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60; // 1시간



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
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION)) // 30분
                .addClaims(Map.of("role", role.name()))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 토큰 검증 및 파싱 메서드
     */
    public Claims validateToken(String token) {

        try {
            // JWT 토큰 파싱 및 유효성 검증
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰일 경우 클레임을 반환
            return e.getClaims();

        } catch (MalformedJwtException e) {
            throw new RuntimeException("유효하지 않은 토큰입니다_1", e);

        } catch (SignatureException e) {
            throw new RuntimeException("유효하지 않은 토큰입니다_2", e);

        } catch (JwtException e) {
            throw new RuntimeException("유효하지 않은 토큰입니다_3", e);

        }
    }


    /**
     * Token 유효 여부 확인 메소드
     * 기존 vaildationToken을 재사용 함
     */
    public boolean isTokenValid(String token) {
        try {
            validateToken(token); // 기존 메서드 재사용
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 토큰 만료 여부 확인 메서드
     */
    public boolean isTokenExpired(String token) {
        return validateToken(token).getExpiration().before(new Date());
    }

    /**
     * Refresh Token 생성 메서드
     */
    public String generateRefreshToken(String userEmail) {
        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION)) // 24시간
                .signWith(secretKey)
                .compact();
    }

    /**
     * Token → memberId 추출
     */
    public String getMemberIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // 이메일 반환
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에서도 클레임을 가져오기 위해 getClaims() 사용
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw new RuntimeException("유효하지 않은 토큰입니다.", e);
        }
    }


    /**
     * Cookie → accessToken 추출
     */
    public String extractAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    System.out.println("Found access token cookie: " + cookie.getValue());  // 디버그 로그 추가
                    return cookie.getValue();
                }
            }
        }
        return null;
    }




}