package com.example.leviathan.member.service;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.db.entity.RefreshToken;
import com.example.leviathan.member.db.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // 로그인 시 발급 (회원당 1개: 기존 토큰은 삭제)
    @Transactional
    public void issue(Member member, String refreshToken, LocalDateTime expiryDate) {
        refreshTokenRepository.deleteByMember(member);
        refreshTokenRepository.save(RefreshToken.issue(member, refreshToken, expiryDate));
    }

    // 로그아웃 시 폐기
    @Transactional
    public int revoke(Member member) {
        return refreshTokenRepository.deleteByMember(member);
    }

    public Optional<RefreshToken> findByMember(Member member) {
        return refreshTokenRepository.findByMember(member);
    }

    // AccessToken 재발급 시 교체
    @Transactional
    public void rotate(RefreshToken refreshToken, String newRefreshToken, LocalDateTime newExpiryDate) {
        refreshToken.rotate(newRefreshToken, newExpiryDate);
        refreshTokenRepository.save(refreshToken);
    }
}
