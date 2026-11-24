package com.example.leviathan.member.db.repository;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.db.entity.RefreshToken;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return jpaRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByMember(Member member) {
        return jpaRepository.findByMember(member);
    }

    @Override
    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return jpaRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public int deleteByMember(Member member) {
        return jpaRepository.deleteByMember(member);
    }
}
