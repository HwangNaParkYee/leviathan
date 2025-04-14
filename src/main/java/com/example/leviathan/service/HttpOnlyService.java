package com.example.leviathan.service;

import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.RefreshToken;
import com.example.leviathan.exception.UnauthorizedException;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.repository.RefreshTokenRepository;
import com.example.leviathan.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class HttpOnlyService {
    private final JwtUtility jwtUtility;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    public String refreshTokenIfNeeded(HttpServletRequest request, HttpServletResponse response) throws UnauthorizedException {
        // 쿠키에서 accessToken 가져오기
        String accessToken = jwtUtility.extractAccessTokenFromCookie(request);

        // accessToken 검증
        if (accessToken == null || !jwtUtility.isTokenValid(accessToken)) {


            // refreshToken이 유효하면, refreshToken을 DB에서 확인하고 갱신
            String email = jwtUtility.getMemberIdFromToken(accessToken);
            Member member = memberRepository.findByMemberId(email);

            // 해당 이메일로 refreshToken 조회
            RefreshToken refreshToken = refreshTokenRepository.findByMember(member).get();


            if (refreshToken == null || !jwtUtility.isTokenValid(refreshToken.getRefreshToken()) || jwtUtility.isTokenExpired(refreshToken.getRefreshToken())) {
                throw new UnauthorizedException("유효하지 않거나 만료된 Refresh Token입니다.");
            }

            // refreshToken이 유효하면 새로운 accessToken 발급
            String newAccessToken = jwtUtility.generateToken(email, member.getRole()); // 새로운 accessToken 생성


            // 새 accessToken을 HttpOnly 쿠키에 저장
            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                    .httpOnly(true)
                    .secure(true)  // HTTPS 환경에서만 전송 (테스트 시 false 가능)
                    .path("/")
                    .maxAge(60 * 5)  // 5분 유지
                    .sameSite("Strict")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

            // refreshToken 갱신: 새로운 refreshToken을 생성하여 DB에 갱신
            String newRefreshToken = jwtUtility.generateRefreshToken(email);
            refreshToken.setRefreshToken(newRefreshToken);
            refreshToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // refreshToken의 만료 시간을 1시간으로 설정

            // DB에서 갱신된 refreshToken 저장
            refreshTokenRepository.save(refreshToken);
        }
        return accessToken;
    }


}
