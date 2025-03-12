package com.example.leviathan.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtUtility {

    private final SecretKey secretKey; // JWT 서명에 사용되는 비밀 키 // 생성한 비밀 키의 타입이 SecretKey 타입

    private static final long expirationTime = 1000 * 60 * 60; // 밀리초 단위 // JWT 만료 시간: 1시간

    // JWT 서명에 사용되는 비밀 키 생성
    public JwtUtility(@Value("${jwt.base64Secret}") String base64Secret) { // @Value을 통해 application.yml에서 값 주입
        this.secretKey = Keys.hmacShaKeyFor(base64Secret.getBytes());
    }                                                    // base64Secret에 64바이트 이상이면 자동으로 HS512 알고리즘 사용

    // JWT 생성
    public String generateToken(String memberId) {
        return "Bearer " + Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // JWT 클레임 반환
    public String getMemberId(String token) {
        // 토큰 파싱 및 클레임 반환
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // 유효한 경우, 클레임 반환
    }

    // JWT 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
        } catch (SignatureException e) {
            System.out.println("Invalid signature");
        } catch (MalformedJwtException e) {
            System.out.println("Invalid token");
        }
        return false;
    }

    // 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);        // "Bearer " 문자 이후의 토큰 부분을 반환
        }
        return null;
    }

}