package com.example.leviathan.member.db.repository;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.db.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByMember(Member member);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    int deleteByMember(Member member);
}
