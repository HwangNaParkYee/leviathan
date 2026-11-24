package com.example.leviathan.member.db.repository;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.db.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMember(Member member);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    int deleteByMember(Member member);

}
