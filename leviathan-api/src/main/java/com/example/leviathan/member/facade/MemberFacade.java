package com.example.leviathan.member.facade;

import com.example.leviathan.common.JwtUtility;
import com.example.leviathan.member.controller.request.LoginRequest;
import com.example.leviathan.member.controller.request.SignUpRequest;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.exception.MemberErrorCode;
import com.example.leviathan.member.exception.MemberException;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.member.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFacade {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtility jwtUtility;

    @Transactional
    public void signUp(SignUpRequest dto) {
        memberService.register(dto.memberId(), passwordEncoder.encode(dto.password()));
    }

    @Transactional
    public String login(LoginRequest dto) {
        Member member = memberService.findByLoginId(dto.memberId());

        if (member == null || !passwordEncoder.matches(dto.password(), member.getPassword())) {
            throw new MemberException(MemberErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtUtility.generateToken(dto.memberId(), member.getRole());
        String refreshToken = jwtUtility.generateRefreshToken(dto.memberId());

        refreshTokenService.issue(member, refreshToken, LocalDateTime.now().plus(JwtUtility.REFRESH_TOKEN_VALIDITY));

        return accessToken;
    }

    @Transactional
    public void deleteMember(Long memberId) {
        if (!memberService.delete(memberId)) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @Transactional
    public int logout(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return refreshTokenService.revoke(member);
    }
}
