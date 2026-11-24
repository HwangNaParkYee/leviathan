package com.example.leviathan.common;

import com.example.leviathan.common.exception.UnauthorizedException;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.db.entity.RefreshToken;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.member.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {

    private final JwtUtility jwtUtility;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public String reissueAccessToken(String expiredAccessToken) {
        String loginId = jwtUtility.getMemberIdFromToken(expiredAccessToken);

        Member member = memberService.findByLoginId(loginId);
        if (member == null) {
            throw new UnauthorizedException("존재하지 않는 회원입니다.");
        }

        RefreshToken refreshToken = refreshTokenService.findByMember(member)
                .orElseThrow(() -> new UnauthorizedException("유효하지 않거나 만료된 Refresh Token입니다."));

        if (refreshToken.isExpired(LocalDateTime.now()) || !jwtUtility.isTokenValid(refreshToken.getRefreshToken())) {
            throw new UnauthorizedException("유효하지 않거나 만료된 Refresh Token입니다.");
        }

        refreshTokenService.rotate(
                refreshToken,
                jwtUtility.generateRefreshToken(loginId),
                LocalDateTime.now().plus(JwtUtility.REFRESH_TOKEN_VALIDITY)
        );

        return jwtUtility.generateToken(loginId, member.getRole());
    }
}
