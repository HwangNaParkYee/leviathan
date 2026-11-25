package com.example.leviathan.common;

import org.springframework.http.ResponseCookie;

public final class AccessTokenCookie {

    public static final String NAME = "accessToken";

    private AccessTokenCookie() {
    }

    public static ResponseCookie of(String accessToken) {
        return ResponseCookie.from(NAME, accessToken)
                .httpOnly(true)
                .secure(true)  // HTTPS 환경에서만 전송 (테스트 시 false 가능)
                .path("/")
                .maxAge(JwtUtility.REFRESH_TOKEN_VALIDITY)
                .sameSite("Strict")
                .build();
    }

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
