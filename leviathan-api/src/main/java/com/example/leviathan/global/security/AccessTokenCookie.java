package com.example.leviathan.global.security;

import org.springframework.http.ResponseCookie;

// accessToken을 담는 HttpOnly 쿠키
public final class AccessTokenCookie {

    public static final String NAME = "accessToken";

    private AccessTokenCookie() {
    }

    // 쿠키는 RefreshToken 유효 기간만큼 유지한다.
    // (AccessToken이 만료되어도 쿠키가 전송되어야 재발급할 수 있다)
    public static ResponseCookie of(String accessToken) {
        return ResponseCookie.from(NAME, accessToken)
                .httpOnly(true)
                .secure(true)  // HTTPS 환경에서만 전송 (테스트 시 false 가능)
                .path("/")
                .maxAge(JwtUtility.REFRESH_TOKEN_VALIDITY)
                .sameSite("Strict")
                .build();
    }

    // 로그아웃 시 쿠키 삭제용
    public static ResponseCookie expired() {
        return ResponseCookie.from(NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)  // 쿠키의 만료 시간을 0으로 설정하여 삭제
                .sameSite("Strict")
                .build();
    }
}
